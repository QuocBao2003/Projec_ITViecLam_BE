package com.example.demo.controller;


import com.example.demo.domain.Permission;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.service.PermissionService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        //check module , apiPath,method
       if(this.permissionService.existsByModuleAndApiPathAndMethod(permission)){
           throw new IdInvalidException("Permission đã tồn tại");
       }
       return ResponseEntity.ok().body(this.permissionService.savePermission(permission));
    }
    @PutMapping("/permissions")
    @ApiMessage("update a permission")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws IdInvalidException {
        if(this.permissionService.fetchById(permission.getId()) == null){
            throw new IdInvalidException("Permission id"+permission.getId()+"không tồn tại");

        }
        //check module , apiPath,method
        if(this.permissionService.existsByModuleAndApiPathAndMethod(permission)){
            if(this.permissionService.isSameName(permission)) {
                throw new IdInvalidException("Permission đã tồn tại");
            }
        }
        return ResponseEntity.ok().body(this.permissionService.updatePermission(permission));
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("get permission by id")
    public ResponseEntity<Permission> getPermissioneById(@PathVariable("id") long id){
        return ResponseEntity.ok().body(this.permissionService.fetchById(id));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete permission")
    public ResponseEntity<Void> deletedPermission(@PathVariable("id") long id) throws IdInvalidException {
        if(this.permissionService.fetchById(id) == null){
            throw new IdInvalidException("Permission không tồn tại");
        }
        this.permissionService.deteled(id);
        return ResponseEntity.ok(null);
    }
    @GetMapping("/permissions")
    @ApiMessage("get all permission ")
    public ResponseEntity<ResultPaginationDTO>  getAllPermission(
            @Filter Specification<Permission> spec,
            Pageable pageable
            ){
        return ResponseEntity.ok().body(this.permissionService.getAllPermission(spec,pageable));
    }
}
