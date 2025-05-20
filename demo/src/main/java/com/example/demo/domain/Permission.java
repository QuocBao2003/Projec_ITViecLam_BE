package com.example.demo.domain;


import com.example.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "name không được rỗng")
    private String name;
    @NotBlank(message = "apiPath không được rỗng")
    private String apiPath;
    @NotBlank(message = "method không được rỗng")
    private String method;

    @NotBlank(message = "module không được rỗng")
    private String module;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
   @ManyToMany(fetch = FetchType.LAZY,mappedBy = "permissions")
   @JsonIgnore
   private List<Role> roles;
    @PrePersist
    public void handleBeforeCreateAt(){
        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get() : ""
        ;
        this.createdAt=Instant.now();
    }
    @PreUpdate
    public void handelBeforeUpdateAt(){
        this.updatedAt=Instant.now();
        this.updatedBy= SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get() : ""
        ;
    }
    public Permission(String name, String apiPath, String method, String module) {
        this.name=name;
        this.apiPath=apiPath;
        this.method=method;
        this.module=module;
    }

}
