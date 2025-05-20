package com.example.demo.controller;

import com.example.demo.config.SecurityConfiguration;
import com.example.demo.domain.User;
import com.example.demo.dto.response.ResCreateUserDTO;
import com.example.demo.dto.response.ResUpdateUserDTO;
import com.example.demo.dto.response.ResUserDTO;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.service.UserService;

import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final SecurityConfiguration securityConfiguration;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, SecurityConfiguration securityConfiguration, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.securityConfiguration = securityConfiguration;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) throws IdInvalidException {
        boolean isEmailexit = this.userService.isEmailExist(postManUser.getEmail());
        if(isEmailexit) {
            throw new IdInvalidException("Email"+ postManUser.getEmail()+"đã tồn tại");
        }
        String hashpassword = passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashpassword);
        User nUser= this.userService.saveUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToRestCreateUserDTO(nUser));
    }
    @ApiMessage("Delete user")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
       User deleteUser =this.userService.getUserById(id);
       if(deleteUser == null) {
           throw  new IdInvalidException("User với id="+id+"không tìm thấy");
       }
       this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }
    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
         User  user= this.userService.getUserById(id);
         if(user == null) {
             throw new IdInvalidException("User với id ="+id+"không tồn tại");
         }
         return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToRestUserDTO(user));
    }
    @GetMapping("/users")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable
            ) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers(spec,pageable));
    }
    @PutMapping("/users")
    @ApiMessage("Update người dùn")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User postManUser) throws IdInvalidException {
       User updateUser = this.userService.updateUser(postManUser);
       if(updateUser == null) {
           throw  new IdInvalidException("User với id ="+updateUser.getId()+"không tồn tại");
       }
       return ResponseEntity.ok(this.userService.convertToRestUpdateUserDTO(updateUser));

    }
    @PutMapping("/users/{id}")
    @ApiMessage("Change user password")
    public ResponseEntity<?> changePassword(@PathVariable("id") Long id, @RequestBody Map<String, String> body) throws IdInvalidException {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        User currentUser = this.userService.getUserById(id);

        System.out.println("Kết quả: "+ passwordEncoder.matches(oldPassword, currentUser.getPassword()));
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            System.out.println("Password in new : " + newPassword);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu cũ không đúng");
        }
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        this.userService.handleUpdatePassword(currentUser);

        return ResponseEntity.ok(this.userService.convertToRestUpdateUserDTO(currentUser));
    }
}
