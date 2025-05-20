package com.example.demo.repository;

import com.example.demo.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill,Long>, JpaSpecificationExecutor<Skill> {
    boolean existsByName(String name);
    List<Skill> findByIdIn(List<Long> id);
}
