package com.example.demo.controller;

import com.example.demo.domain.Job;
import com.example.demo.dto.response.job.ResCreateJobDTO;
import com.example.demo.dto.response.job.ResUpdateJobDTO;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.service.JobService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a new job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.createJob(job));

    }
    @PutMapping("/jobs")
    @ApiMessage("Update job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> currnentJob = this.jobService.getJobById(job.getId());
        if(!currnentJob.isPresent()){
            throw  new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok().body(this.jobService.updateJob(job,currnentJob.get()));
    }
    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete job")
    public ResponseEntity<Void> handleDeleteJob(@PathVariable("id") long id){
        this.jobService.deleteJob(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> job = this.jobService.getJobById(id);
        if(!job.isPresent()){
            throw  new IdInvalidException("Job not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(job.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("GET JOBs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(
            @Filter Specification<Job> spec,
            Pageable pageable
            ){
        return ResponseEntity.ok(this.jobService.getAllJob(spec,pageable));
    }

}
