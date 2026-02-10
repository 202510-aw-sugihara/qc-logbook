package com.example.qclogbook.domain.entity;

import com.example.qclogbook.domain.enums.EnvironmentType;
import com.example.qclogbook.domain.enums.Judgement;
import com.example.qclogbook.domain.enums.TriggerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ソーク試験セッションを表すエンティティ。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "soak_sessions",
    indexes = {
        @Index(name = "idx_soak_sessions_inspection", columnList = "inspection_id"),
        @Index(name = "idx_soak_sessions_env_type", columnList = "env_type")
    }
)
public class SoakSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    private Inspection inspection;

    @Enumerated(EnumType.STRING)
    @Column(name = "env_type", nullable = false, length = 20)
    private EnvironmentType envType;

    @Enumerated(EnumType.STRING)
    @Column(name = "judgement", nullable = false, length = 20)
    private Judgement judgement;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false, length = 20)
    private TriggerType triggerType;

    @Column(name = "target_temp_c", nullable = false)
    private Integer targetTempC;

    @Column(name = "tolerance_c", nullable = false)
    private Integer toleranceC;

    @Column(name = "target_voltage_v", nullable = false, precision = 10, scale = 2)
    private BigDecimal targetVoltageV;

    @Column(name = "window_before_ms", nullable = false)
    private Integer windowBeforeMs;

    @Column(name = "window_after_ms", nullable = false)
    private Integer windowAfterMs;

    @Column(name = "trigger_at")
    private LocalDateTime triggerAt;

    @Column(name = "trigger_temp_c", precision = 10, scale = 2)
    private BigDecimal triggerTempC;

    @Column(name = "summary_min_c", precision = 10, scale = 2)
    private BigDecimal summaryMinC;

    @Column(name = "summary_max_c", precision = 10, scale = 2)
    private BigDecimal summaryMaxC;

    @Column(name = "summary_avg_c", precision = 10, scale = 2)
    private BigDecimal summaryAvgC;

    @Column(name = "out_of_spec_count")
    private Integer outOfSpecCount;

    // 大量データ想定のため、安易な一括カスケードは行わない
    @OneToMany(mappedBy = "session", orphanRemoval = false)
    private List<TempPoint> points = new ArrayList<>();

    /**
     * デフォルト条件でSoakSessionを生成する。
     */
    public static SoakSession createDefault(
        Inspection inspection,
        EnvironmentType envType,
        Integer targetTempC,
        Integer toleranceC,
        BigDecimal targetVoltageV,
        Integer windowBeforeMs,
        Integer windowAfterMs
    ) {
        SoakSession session = new SoakSession();
        session.inspection = inspection;
        session.envType = envType;
        session.judgement = Judgement.PENDING;
        session.triggerType = TriggerType.V_REACHED;
        session.targetTempC = targetTempC;
        session.toleranceC = toleranceC;
        session.targetVoltageV = targetVoltageV;
        session.windowBeforeMs = windowBeforeMs;
        session.windowAfterMs = windowAfterMs;
        session.outOfSpecCount = 0;
        return session;
    }

    @PrePersist
    private void prePersist() {
        if (judgement == null) {
            this.judgement = Judgement.PENDING;
        }
        if (triggerType == null) {
            this.triggerType = TriggerType.V_REACHED;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SoakSession)) {
            return false;
        }
        SoakSession other = (SoakSession) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}