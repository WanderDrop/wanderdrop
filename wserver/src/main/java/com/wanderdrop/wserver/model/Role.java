package com.wanderdrop.wserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name= "role")
public class Role {

    @Id
    @Column(name= "role_id", nullable = false)
    private String roleId;

    @Column(name= "role_name", unique = true, nullable = false)
    private String roleName;

}
