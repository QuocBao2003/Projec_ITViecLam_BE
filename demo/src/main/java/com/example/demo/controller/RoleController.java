package com.example.demo.controller;


import com.example.demo.domain.Role;
import com.example.demo.dto.response.ResultPaginationDTO;
import com.example.demo.service.RoleService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService  roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create a role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException{
        if(this.roleService.existsByName(role.getName())){
            throw new IdInvalidException("Role đã tồn tại");
        }
        return ResponseEntity.ok(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("update role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException{
        if(this.roleService.getById(role.getId())==null){
            throw new IdInvalidException("Role không tồn tại");
        }
//        if(this.roleService.existsByName(role.getName())){
//            throw new IdInvalidException("Role với name"+role.getName()+"đã tồn tại");
//        }
        return ResponseEntity.ok(this.roleService.updateRole(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws IdInvalidException{
//        ckeck id
        if(this.roleService.getById(id)==null){
            throw new IdInvalidException("Role với id="+id+"khoong tồn tại");
        }
        this.roleService.deleteRoleById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Get role with page")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(
            @Filter Specification<Role> spec, Pageable pageable
            ){
        return ResponseEntity.ok(this.roleService.getAllRole(spec,pageable));

    }
    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role by id")
    public ResponseEntity<Role> fetchRoleByid(@PathVariable("id") long id) throws IdInvalidException{
        Role role=this.roleService.getById(id);
        if(role==null){
            throw new IdInvalidException("Role với id"+id+"không tồn tại");
        }
        return ResponseEntity.ok(role);
    }
}

