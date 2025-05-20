package com.example.demo.controller;

import com.example.demo.domain.Skill;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.repository.SkillRepository;
import com.example.demo.service.SkillService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;
    private final SkillRepository skillRepository;

    public SkillController(SkillService skillService, SkillRepository skillRepository) {
        this.skillService = skillService;
        this.skillRepository = skillRepository;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> handleCreateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if(skill.getName() != null && this.skillService.isNameExit(skill.getName())) {
            throw new IdInvalidException("Skill đã tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleSaveSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> handleUpdateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill currentSkill =this.skillService.getSkillById(skill.getId());
        if(currentSkill == null){
            throw new IdInvalidException("skill id"+skill.getId()+"không tồn tại");
        }
        if(skill.getName() != null && this.skillService.isNameExit(skill.getName())) {
            throw  new IdInvalidException("Skill name"+skill.getName()+"đã tồn tại");
        }
        currentSkill.setName(skill.getName());
        return ResponseEntity.ok(this.skillService.updateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skill")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(
            @Filter Specification<Skill> spec,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.getAllSkill(spec,pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkillById(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.getSkillById(id);
        if(currentSkill == null){
            throw new IdInvalidException("Skill id"+id+"không tồn tại");
        }
        this.skillService.deleteSkillById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
