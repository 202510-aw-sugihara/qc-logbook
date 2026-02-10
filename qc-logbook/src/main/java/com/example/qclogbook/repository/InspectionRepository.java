package com.example.qclogbook.repository;

import com.example.qclogbook.domain.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Inspectionの永続化を行うリポジトリ。
 */
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
}