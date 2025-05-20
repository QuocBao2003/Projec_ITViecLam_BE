package com.example.demo.domain;

import com.example.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "companies")

public class Company {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "companyName không được rỗng")
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a",timezone = "GMT+7")
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;

    @PrePersist
    public void handleBeforeCreateAt(){
      this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()==true ?
      SecurityUtil.getCurrentUserLogin().get() : "";
      this.createdAt=Instant.now();
    }

    @PreUpdate
    public void hanleBeforeUpdateAt(){
        this.updatedAt=Instant.now();
        this.updatedBy=SecurityUtil.getCurrentUserLogin().isPresent() == true?
                SecurityUtil.getCurrentUserLogin().get() : "";
    }
}
