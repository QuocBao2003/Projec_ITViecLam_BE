package com.example.demo.controller;


import com.example.demo.domain.Company;

import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.service.CompanyService;
import com.example.demo.util.annotation.ApiMessage;
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
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company createdCompany = companyService.saveCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
//            @RequestParam("current")Optional<String> currentOptional,
//            @RequestParam("pageSize") Optional<String> pageSizeOptional
            @Filter Specification<Company> spec, //tim kiem theo filter
            Pageable pageable//phan trang, size
            ) {
//        String sCurrent=currentOptional.isPresent()? currentOptional.get():"";
//        String sPageSiz=pageSizeOptional.isPresent()? pageSizeOptional.get():"";
//        int current = Integer.parseInt(sCurrent);
//        int pageSize =Integer.parseInt(sPageSiz);
//        Pageable pageable= PageRequest.of(current-1,pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(companyService.findAllCompany(spec,pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        Company updateCompany = this.companyService.updateCompany(reqCompany);
        return ResponseEntity.status(HttpStatus.OK).body(updateCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCCompany(id);
        return ResponseEntity.ok(null);

    }
    @GetMapping("/companies/{id}")
    @ApiMessage("get company by id")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") long id){
        Optional<Company> reqCompany = this.companyService.findById(id);
        return ResponseEntity.ok(reqCompany.get());
    }
}
