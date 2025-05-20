package com.example.demo.service;

import com.example.demo.domain.Skill;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.repository.SkillRepository;
import com.example.demo.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    public Skill handleSaveSkill(Skill skill) throws IdInvalidException{

        return this.skillRepository.save(skill);
    }
    public Boolean isNameExit(String name){
        return this.skillRepository.existsByName(name);
    }
    public ResultPaginationDTO getAllSkill(Specification<Skill> spec, Pageable pageable){
        Page<Skill> pageSkill = this.skillRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageSkill.getTotalElements());
        mt.setPages(pageSkill.getTotalPages());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());
        return rs;
    }

    public Skill getSkillById(long id) {
        Optional<Skill> skill = this.skillRepository.findById(id);
        if(skill.isPresent()) {
            return skill.get();
        }
        return null;
    }
    public Skill updateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }
    public void deleteSkillById(long id) {
//        deltee job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job ->job.getSkills().remove(currentSkill) );
//        delete subscriber (inside subscriber_skill)
        currentSkill.getSubscribers().forEach(subs->subs.getSkills().remove(currentSkill));
//        deleted skill
        this.skillRepository.delete(currentSkill);
    }

}

