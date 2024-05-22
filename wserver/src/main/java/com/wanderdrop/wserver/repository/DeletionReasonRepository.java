package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.DeletionReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletionReasonRepository extends JpaRepository<DeletionReason, Long> {
}
