package com.example.demo.service;


import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;

        this.permissionRepository = permissionRepository;
    }
    public Role getById(long id){

        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }
    public boolean existsByName(String name){
        return this.roleRepository.existsByName(name);
    }

    public Role createRole(Role role){
//        check permission
        if(role.getPermissions()!= null){
            List<Long> reqPermissions =role.getPermissions()
                    .stream().map(permission -> permission.getId()).collect(Collectors.toList()); //[1,2,...]
//            truyền lên tất cả id của permission
            List<Permission> permissions=this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(permissions);
        }

        return this.roleRepository.save(role);
    }
    public Role updateRole(Role role){
        Role roleDB = this.roleRepository.getById(role.getId());
        if(role.getPermissions()!=null){
            List<Long> reqPermissions = role.getPermissions().stream().map(permission -> permission.getId()).collect(Collectors.toList());
            List<Permission> permissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(permissions);
        }
        roleDB.setName(role.getName());
        roleDB.setDescription(role.getDescription());
        roleDB.setPermissions(role.getPermissions());
       roleDB.setActive(role.isActive());
       roleDB=this.roleRepository.save(roleDB);
       return roleDB;

    }
    public void deleteRoleById(long id){
        this.roleRepository.deleteById(id);
    }
    public ResultPaginationDTO getAllRole(Specification<Role> spec, Pageable pageable){
        Page<Role> pageRole = this.roleRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageRole.getTotalElements());
        mt.setPages(pageRole.getTotalPages());
        rs.setMeta(mt);
        rs.setResult(pageRole.getContent());
        return rs;
    }
}
