package com.example.qclogbook.repository;

import com.example.qclogbook.domain.entity.TempPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TempPointの永続化を行うリポジトリ。
 */
public interface TempPointRepository extends JpaRepository<TempPoint, Long> {

    List<TempPoint> findBySessionIdOrderByOffsetMsAsc(Long sessionId);
}