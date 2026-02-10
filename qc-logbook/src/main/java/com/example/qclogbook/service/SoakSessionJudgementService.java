package com.example.qclogbook.service;

import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.domain.entity.TempPoint;
import com.example.qclogbook.domain.enums.Judgement;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * SoakSessionのサマリと判定を再計算するサービス。
 */
@Service
public class SoakSessionJudgementService {

    public void recompute(SoakSession session, List<TempPoint> points) {
        if (points == null || points.isEmpty()) {
            session.updateSummary(
                null,
                null,
                null,
                0,
                null,
                Judgement.PENDING
            );
            return;
        }

        BigDecimal min = null;
        BigDecimal max = null;
        BigDecimal sum = BigDecimal.ZERO;
        int outOfSpecCount = 0;
        BigDecimal triggerTempC = null;

        BigDecimal lower = BigDecimal.valueOf(session.getTargetTempC())
            .subtract(BigDecimal.valueOf(session.getToleranceC()));
        BigDecimal upper = BigDecimal.valueOf(session.getTargetTempC())
            .add(BigDecimal.valueOf(session.getToleranceC()));

        for (TempPoint point : points) {
            BigDecimal tempC = point.getTempC();
            if (tempC == null) {
                continue;
            }

            if (min == null || tempC.compareTo(min) < 0) {
                min = tempC;
            }
            if (max == null || tempC.compareTo(max) > 0) {
                max = tempC;
            }

            sum = sum.add(tempC);

            if (tempC.compareTo(lower) < 0 || tempC.compareTo(upper) > 0) {
                outOfSpecCount++;
            }

            if (point.getOffsetMs() != null && point.getOffsetMs() == 0) {
                triggerTempC = tempC;
            }
        }

        BigDecimal avg = sum.divide(BigDecimal.valueOf(points.size()), 2, RoundingMode.HALF_UP);

        Judgement judgement = Judgement.FAIL;
        if (outOfSpecCount == 0 && triggerTempC != null
            && triggerTempC.compareTo(lower) >= 0 && triggerTempC.compareTo(upper) <= 0) {
            judgement = Judgement.PASS;
        }

        session.updateSummary(min, max, avg, outOfSpecCount, triggerTempC, judgement);
    }
}
