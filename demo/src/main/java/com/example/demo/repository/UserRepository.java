package com.example.demo.repository;

import com.example.demo.domain.Company;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    boolean existsUserByEmail(String email);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
    List<User> findByCompany(Company company);
}
