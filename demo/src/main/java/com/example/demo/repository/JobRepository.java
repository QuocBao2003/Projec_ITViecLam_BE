package com.example.demo.repository;

import com.example.demo.domain.Job;
import com.example.demo.domain.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job,Long> {
    boolean existsJobByName(String name);

    Page<Job> findAll(Specification<Job> spec, Pageable pageable);
//    tìm danh sách công việc chứa skill
    List<Job> findBySkillsIn(List<Skill> skills);
}
