package com.example.demo.service;

import com.example.demo.domain.Job;
import com.example.demo.domain.Resume;
import com.example.demo.domain.User;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.dto.response.resume.ResCreateResumeDTO;
import com.example.demo.dto.response.resume.ResFetchResumeDTO;
import com.example.demo.dto.response.resume.ResUpdateResumeDTO;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.ResumeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.SecurityUtil;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService  {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    @Autowired
   FilterBuilder filterBuilder;
    @Autowired
    private  FilterParser filterParser;
    @Autowired
    private  FilterSpecificationConverter filterSpecificationConverter;
    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository ) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;

    }

    public boolean checkResumeExistsByUserAndJob(Resume resume){
//        check user by id
        if(resume.getUser()==null){
            return false;
        }
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if(userOptional.isEmpty()){
            return false;
        }
//        check job by id
        if(resume.getJob()==null){
            return false;
        }
        Optional<Job> optionalJob = this.jobRepository.findById(resume.getJob().getId());
        if(optionalJob.isEmpty()){
            return false;
        }
        return true;
    }

    public ResCreateResumeDTO createResume(Resume resume){
        resume=this.resumeRepository.save(resume);
        ResCreateResumeDTO rs = new ResCreateResumeDTO();
        rs.setId(resume.getId());
        rs.setCreatedAt(resume.getCreatedAt());
        rs.setCreatedBy(resume.getCreatedBy());
        return rs;
    }
    public ResUpdateResumeDTO update(Resume resume){
        resume=this.resumeRepository.save(resume);
        ResUpdateResumeDTO rs = new ResUpdateResumeDTO();
        rs.setUpdatedAt(resume.getUpdatedAt());
        rs.setUpdatedBy(resume.getUpdatedBy());

        return rs;
    }
    public Optional<Resume> getResumeById(long id){
        return this.resumeRepository.findById(id);
    }
    public void deleteResumeById(long id){
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume){
        ResFetchResumeDTO reqFetchResume = new ResFetchResumeDTO();
        reqFetchResume.setId(resume.getId());
        reqFetchResume.setEmail(resume.getEmail());
        reqFetchResume.setUrl(resume.getUrl());
        reqFetchResume.setStatus(resume.getStatus());
        reqFetchResume.setCreatedAt(resume.getCreatedAt());
        reqFetchResume.setCreatedBy(resume.getCreatedBy());
        reqFetchResume.setUpdatedAt(resume.getUpdatedAt());
        reqFetchResume.setUpdatedBy(resume.getUpdatedBy());
        if(resume.getJob()!=null){
            reqFetchResume.setCompanyName(resume.getJob().getCompany().getName());
        }
        reqFetchResume.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(),resume.getUser().getName()));
        reqFetchResume.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(),resume.getJob().getName()));
        return reqFetchResume;
    }

    public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable){
        Page<Resume> pageResume = this.resumeRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageResume.getTotalElements());
        mt.setPages(pageResume.getTotalPages());
        rs.setMeta(mt);
        rs.setResult(pageResume.getContent());
        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(item ->this.getResume(item)).collect(Collectors.toList());
        rs.setResult(listResume);
        return rs;
    }
//    lấy toàn bộ resume by user login
public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
    // query builder
    String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
            ? SecurityUtil.getCurrentUserLogin().get()
            : "";
    FilterNode node = filterParser.parse("email='" + email + "'");
    FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
    Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());

    mt.setPages(pageResume.getTotalPages());
    mt.setTotal(pageResume.getTotalElements());

    rs.setMeta(mt);

    // remove sensitive data
    List<ResFetchResumeDTO> listResume = pageResume.getContent()
            .stream().map(item -> this.getResume(item))
            .collect(Collectors.toList());

    rs.setResult(listResume);

    return rs;
}
}
