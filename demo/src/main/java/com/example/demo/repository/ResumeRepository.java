package com.example.demo.repository;

import com.example.demo.domain.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Page<Resume> findAll(Specification<Resume> spec, Pageable pageable);
}
