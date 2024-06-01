package com.wanderdrop.wserver.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Data
@Table(name= "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "report_id")
    private Long reportId;

    @Column(name= "report_heading", nullable = false)
    private String reportHeading;

    @Column(name= "report_message", nullable = false)
    private String reportMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_id",referencedColumnName = "attraction_id", nullable = false, updatable = false )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Attraction attractionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by",referencedColumnName = "user_id", nullable = false, updatable = false )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User createdBy;

    @Column(name= "created_at", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

}
