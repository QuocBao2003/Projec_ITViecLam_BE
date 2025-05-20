package com.example.demo.domain;



import com.example.demo.util.Enum.LevelEnum;
import com.example.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private long id;
    @NotBlank(message = "name không được để trống")
    private String name;
    @NotBlank(message = "location không được để trống")
    private String location;
    private double salary;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private LevelEnum level;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

   @ManyToMany(fetch = FetchType.LAZY)
   @JsonIgnoreProperties(value = {"jobs"})
   @JoinTable(name = "job_skill",
    joinColumns = @JoinColumn(name = "job_id"),
   inverseJoinColumns = @JoinColumn(name = "skill_id"))
   private List<Skill> skills;

   @OneToMany(mappedBy = "job",fetch = FetchType.LAZY)
   @JsonIgnore
   private List<Resume> resumes;
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
