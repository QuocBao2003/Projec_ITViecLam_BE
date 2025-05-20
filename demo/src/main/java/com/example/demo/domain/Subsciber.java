package com.example.demo.domain;


import com.example.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscribers")
public class Subsciber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "email không được để trống")
    private String email;
    @NotBlank(message = "name không được để trống")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"subscribers"})
    @JoinTable(name = "subscriber_skill",
    joinColumns = @JoinColumn(name = "subscriber_id"),
    inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
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
