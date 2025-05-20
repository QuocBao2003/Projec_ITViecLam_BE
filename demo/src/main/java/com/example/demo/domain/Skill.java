package com.example.demo.domain;


import com.example.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "name không được để trống")
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "skills")
    @JsonIgnore
    private List<Subsciber> subscribers;
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
