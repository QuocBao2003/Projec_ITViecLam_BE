package com.example.demo.service;


import com.example.demo.domain.Job;
import com.example.demo.domain.Skill;
import com.example.demo.domain.Subsciber;
import com.example.demo.dto.response.email.ResEmailJob;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.SkillRepository;
import com.example.demo.repository.SubsccibersRepository;
import com.example.demo.util.error.IdInvalidException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscribersService{

    private final SubsccibersRepository subsccibersRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscribersService(SubsccibersRepository subsccibersRepository, SkillRepository skillRepository, JobRepository jobRepository, EmailService emailService) {
        this.subsccibersRepository = subsccibersRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public boolean existsByEmail(String email){
        return this.subsccibersRepository.existsByEmail(email);
    }
    public Subsciber getSubsciberById(long id){
        Optional<Subsciber> subsciberOptional = this.subsccibersRepository.findById(id);
        if(subsciberOptional.isPresent())
            return subsciberOptional.get();
        return null;
    }
//    @Scheduled(cron = "*/10 * * * * *")
//    public void testCron(){
//        System.out.println("test cron job");
//    }
    public Subsciber createSubsciber(Subsciber subsciber) {
//        check skill
        if(subsciber.getSkills() != null){
            List<Long> reqSkills = subsciber.getSkills().stream().map(x->x.getId()).collect(Collectors.toList());
            List<Skill> skill =  this.skillRepository.findByIdIn(reqSkills);
            subsciber.setSkills(skill);
        }
        return this.subsccibersRepository.save(subsciber);
    }
    public Subsciber findByEmail(String email){
        return this.subsccibersRepository.findByEmail(email);
    }

    public Subsciber updateSubsciber(Subsciber subsDB,Subsciber subsRequest) {
        if(subsRequest.getSkills() != null){
            List<Long> reqSkills = subsRequest.getSkills().stream().map(x->x.getId()).collect(Collectors.toList());
            List<Skill> skill =  this.skillRepository.findByIdIn(reqSkills);
            subsDB.setSkills(skill);
        }
        return this.subsccibersRepository.save(subsDB);
    }
    public ResEmailJob convertJobToEmail(Job job){
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> skillEmail = skills.stream().map(x-> new ResEmailJob.SkillEmail(x.getName())).collect(Collectors.toList());
        res.setSkills(skillEmail);
        return res;
    }
    public void sendSubscribersEmailJobs() {
        List<Subsciber> listSubs = this.subsccibersRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subsciber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                         List<ResEmailJob> arr = listJobs.stream().map(
                         job -> this.convertJobToEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);

                    }
                }
            }
        }
    }


}
