package com.example.demo.service;

import com.example.demo.domain.Permission;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean existsByModuleAndApiPathAndMethod(Permission permission){
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(),permission.getApiPath(),permission.getMethod());
    }

    public Permission fetchById(long id){
        Optional<Permission> permission = this.permissionRepository.findById(id);
        if(permission.isPresent()){
            return permission.get();
        }
        return null;
    }

    public Permission savePermission(Permission permission){
        return this.permissionRepository.save(permission);
    }
    public Permission updatePermission(Permission permission){
        Optional<Permission> permissionOptional = this.permissionRepository.findById(permission.getId());
        if(permissionOptional.isPresent()){
            Permission currentPermission = permissionOptional.get();
            currentPermission.setName(permission.getName());
            currentPermission.setModule(permission.getModule());
            currentPermission.setApiPath(permission.getApiPath());
            currentPermission.setMethod(permission.getMethod());
            return this.permissionRepository.save(currentPermission);
        }
        return null;
    }
    public void deteled(long id){
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if(permissionOptional.isPresent()){
            Permission currentPermission = permissionOptional.get();
            currentPermission.getRoles().forEach(role ->role.getPermissions().remove(currentPermission));
            this.permissionRepository.delete(currentPermission);
        }
    }

    public ResultPaginationDTO getAllPermission(Specification<Permission> spec, Pageable pageable){
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pagePermission.getContent());
        return rs;
    }
    public boolean isSameName(Permission permission){
        Permission p = this.fetchById(permission.getId());
        if(p!=null){
            if(p.getName().equals(permission.getName())){
                return true;
            }
        }
        return false;
    }
}
