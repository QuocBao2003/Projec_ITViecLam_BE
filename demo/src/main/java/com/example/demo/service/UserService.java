package com.example.demo.service;

import com.example.demo.domain.Company;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.response.ResCreateUserDTO;
import com.example.demo.dto.response.ResUpdateUserDTO;
import com.example.demo.dto.response.ResUserDTO;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository, RoleService roleService) {
        this.userRepository = userRepository;

        this.companyRepository = companyRepository;
        this.roleService = roleService;
    }

    public User saveUser(User user) {
//        check company
        if(user.getCompany() != null){
            Optional<Company> companyOptional= this.companyRepository.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ?
                    companyOptional.get() : null);
        }
//        check role
        if(user.getRole()!=null){
            Role role = this.roleService.getById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

        return userRepository.save(user);
    }
    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }
    public User getUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }
    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable)
    {
        Page<User> pageUser = this.userRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);//tu font-end truyen len
        mt.setPageSize(pageable.getPageSize());//tu font-end truyen len
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item ->this.convertToRestUserDTO(item)).collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }
    public User updateUser(User reqUser) {
        User currentUser= this.getUserById(reqUser.getId());
        if(currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setAge(reqUser.getAge());
            currentUser.setGender(reqUser.getGender());

//            check company
            if(reqUser.getCompany() != null) {
              Optional<Company> companyOptional= this.companyRepository.findById(reqUser.getCompany().getId());
              currentUser.setCompany(companyOptional.isPresent() ?
                      companyOptional.get() : null);
            }

//        check role
            if(reqUser.getRole()!=null){
              Role role = this.roleService.getById(reqUser.getRole().getId());
                currentUser.setRole(role != null ? role : null);
            }
            currentUser=this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }
    public boolean isEmailExist(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    public ResCreateUserDTO convertToRestCreateUserDTO(User user) {
        ResCreateUserDTO rs = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setEmail(user.getEmail());
        rs.setAddress(user.getAddress());
        rs.setAge(user.getAge());
        rs.setGender(user.getGender());
        rs.setCreatedAt(user.getCreatedAt());
        if(user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            rs.setCompanyUser(companyUser);
        }
        return rs;
    }

    public ResUserDTO convertToRestUserDTO(User user) {
        ResUserDTO rs = new ResUserDTO();
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setEmail(user.getEmail());
        rs.setAddress(user.getAddress());
        rs.setAge(user.getAge());
        rs.setGender(user.getGender());
        rs.setCreatedAt(user.getCreatedAt());
        rs.setUpdatedAt(user.getUpdatedAt());
        if(user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            rs.setCompanyUser(companyUser);
        }
        if(user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            rs.setRoleUser(roleUser);
        }
        return rs;
    }
    public ResUpdateUserDTO convertToRestUpdateUserDTO(User user) {
        ResUpdateUserDTO rs = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setEmail(user.getEmail());
        rs.setAddress(user.getAddress());
        rs.setAge(user.getAge());
        rs.setGender(user.getGender());
        rs.setUpdateAt(user.getUpdatedAt());
        if(user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            rs.setCompanyUser(companyUser);
        }
        return rs;
    }

    public void updateUserToken(String token,String email){
        User currentUser = this.handleGetUserByUserName(email);
        if(currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token,String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
    public void handleUpdatePassword(User user) {
        userRepository.save(user);
    }
}

