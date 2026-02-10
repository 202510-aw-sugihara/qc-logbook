package com.example.qclogbook.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 温度の測定点を表すエンティティ。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "temp_points",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_temp_points_session_offset", columnNames = {"session_id", "offset_ms"})
    },
    indexes = {
        @Index(name = "idx_temp_points_session", columnList = "session_id"),
        @Index(name = "idx_temp_points_session_offset", columnList = "session_id, offset_ms")
    }
)
public class TempPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private SoakSession session;

    @Column(name = "offset_ms", nullable = false)
    private Integer offsetMs;

    @Column(name = "temp_c", nullable = false, precision = 10, scale = 2)
    private BigDecimal tempC;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * セッション未紐付けの測定点を生成する。
     */
    public static TempPoint create(Integer offsetMs, BigDecimal tempC) {
        TempPoint point = new TempPoint();
        point.offsetMs = offsetMs;
        point.tempC = tempC;
        return point;
    }

    /**
     * 測定点にセッションを紐付ける。
     */
    public void assignSession(SoakSession session) {
        this.session = session;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TempPoint)) {
            return false;
        }
        TempPoint other = (TempPoint) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}