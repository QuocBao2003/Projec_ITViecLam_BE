package com.example.demo.domain;


import com.example.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "name không được rỗng")
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"roles"})
    @JoinTable(name = "permission_role",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;


    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    @PrePersist
    public void handleBeforeCreateAt(){
        this.createdAt=Instant.now();
        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get() : "";
    }
    @PreUpdate
    public void handelBeforeUpdateAt(){
        this.updatedAt=Instant.now();
        this.updatedBy=SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get() : "";
    }
}
