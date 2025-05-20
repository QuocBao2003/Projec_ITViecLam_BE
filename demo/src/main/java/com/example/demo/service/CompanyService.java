package com.example.demo.service;

import com.example.demo.domain.Company;
import com.example.demo.domain.User;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

   public Company saveCompany( Company company) {
        return companyRepository.save(company);
   }

   public ResultPaginationDTO findAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
       ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
       mt.setPage(companies.getNumber()+1);
       mt.setPageSize(companies.getSize());
       mt.setPages(companies.getTotalPages());
       mt.setTotal(companies.getTotalElements());
       rs.setMeta(mt);
       rs.setResult(companies.getContent());
       return rs;
   }

   public Company updateCompany(Company company) {
       Optional<Company> companyOptional = companyRepository.findById(company.getId());
       if(companyOptional.isPresent()) {
           Company currentCompany = companyOptional.get();
           currentCompany.setName(company.getName());
           currentCompany.setLogo(company.getLogo());
           currentCompany.setDescription(company.getDescription());
           currentCompany.setAddress(company.getAddress());
           return this.companyRepository.save(currentCompany);
       }
           return null;

   }

   public void deleteCCompany(Long id) {
       Optional<Company> companyOptional = companyRepository.findById(id);
       if(companyOptional.isPresent()) {
           Company com =  companyOptional.get();
//           fetch all user belong to this company
           List<User> userList=this.userRepository.findByCompany(com);
           this.userRepository.deleteAll(userList);
       }
       this.companyRepository.deleteById(id);
   }
   public Optional<Company> findById(long id){
        return this.companyRepository.findById(id);
   }
}
