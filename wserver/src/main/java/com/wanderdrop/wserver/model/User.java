package com.wanderdrop.wserver.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.security.Timestamp;

@Entity
@Data
@Table(name= "user")
public class User {

    @Id
    @GeneratedValue
    @Column(name= "user_id")
    private String userId;

    @Column(name= "username", unique = true, nullable = false)
    private String username;

    @Column(name= "password", nullable = false)
    private String password;

    @Column(name= "email", nullable = false)
    private String email;

    @Column(name= "first_name", nullable = false)
    private String firstName;

    @Column(name= "last_name", nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "role_id", referencedColumnName = "role_id", nullable = false, updatable = false)
    @Column(name= "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Role roleId;

    @Column(name= "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name= "updated_at")
    private Timestamp updatedAt;

    @Column(name= "user_status")
    private UserStatus userStatus;

}
