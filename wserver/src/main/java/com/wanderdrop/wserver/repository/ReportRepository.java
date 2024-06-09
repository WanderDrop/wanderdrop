package com.wanderdrop.wserver.repository;
import com.wanderdrop.wserver.model.Report;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatusAndAttraction_AttractionId(Status status, Long attractionId);
    List<Report> findByStatus(Status status);
}
