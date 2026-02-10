package com.example.qclogbook.service;

import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.domain.entity.TempPoint;
import com.example.qclogbook.repository.SoakSessionRepository;
import com.example.qclogbook.repository.TempPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SoakSessionの再計算を行うサービス。
 */
@Service
public class SoakSessionService {

    private final SoakSessionRepository soakSessionRepository;
    private final TempPointRepository tempPointRepository;
    private final SoakSessionJudgementService judgementService;

    public SoakSessionService(
        SoakSessionRepository soakSessionRepository,
        TempPointRepository tempPointRepository,
        SoakSessionJudgementService judgementService
    ) {
        this.soakSessionRepository = soakSessionRepository;
        this.tempPointRepository = tempPointRepository;
        this.judgementService = judgementService;
    }

    @Transactional
    public void recomputeSession(Long sessionId) {
        SoakSession session = soakSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("SoakSession not found: " + sessionId));

        List<TempPoint> points = tempPointRepository.findBySessionIdOrderByOffsetMsAsc(sessionId);

        judgementService.recompute(session, points);

        soakSessionRepository.save(session);
    }
}