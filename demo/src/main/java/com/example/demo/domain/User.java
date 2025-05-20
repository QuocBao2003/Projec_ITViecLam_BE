package com.example.demo.domain;

import com.example.demo.util.Enum.GenderEnum;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotBlank(message = "password không được để trống")
    private String password;
    @NotBlank(message = "email không được để trống")
    private String email;

    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Resume> resumes;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy=SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get() : "";
    }
}
