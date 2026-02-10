package com.example.qclogbook.repository;

import com.example.qclogbook.domain.entity.SoakSession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SoakSessionの永続化を行うリポジトリ。
 */
public interface SoakSessionRepository extends JpaRepository<SoakSession, Long> {
}