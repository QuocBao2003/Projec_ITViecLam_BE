package com.example.demo.domain;

import com.example.demo.util.Enum.ResumeStateEnum;
import com.example.demo.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "resumes")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "email không được rỗng")
    private String email;
    @NotBlank(message = "url không được để trống (upload cv chưa thành công)")
    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

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
