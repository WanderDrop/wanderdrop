package com.wanderdrop.wserver.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "deletionreason")
public class DeletionReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=" deletion_reason_id")
    private int deletionReasonId;

    @Column(name= "reason_message", nullable = false)
    private String reasonMessage;
}
