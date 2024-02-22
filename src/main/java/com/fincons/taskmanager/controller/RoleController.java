package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.RoleDTO;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.mapper.UserAndRoleMapper;
import com.fincons.taskmanager.service.authService.RoleService;
import com.fincons.taskmanager.utility.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/task-manager")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserAndRoleMapper userAndRoleMapper;

    @GetMapping("${role.list}")
    public ResponseEntity<GenericResponse<List<RoleDTO>>> getAllRoles () {

        List<RoleDTO> listRoleDTOs =  roleService.findAllRoles()
                .stream()
                .map(r -> userAndRoleMapper.roleToRoleDto(r))
                .toList();

        return ResponseEntity.ok(GenericResponse.<List<RoleDTO>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("Role list found!")
                .data(listRoleDTOs)
                .build());
    }

    @GetMapping("${role.find-by-id}/{roleId}")
    public ResponseEntity<GenericResponse<RoleDTO>> getRoleById(@PathVariable long roleId) {
        try{
            RoleDTO roleDTO = userAndRoleMapper.roleToRoleDto(roleService.getRoleById(roleId));

            return ResponseEntity.ok(GenericResponse.<RoleDTO>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .message("Role found Succesfully  !!!")
                    .data(roleDTO)
                    .build());
        }catch(ResourceNotFoundException rnfe){
            return ResponseEntity.ok().body(GenericResponse.<RoleDTO>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .success(false)
                    .message(rnfe.getMessage())
                    .build());
        }

    }

    @PostMapping("${role.create}")
    public ResponseEntity<GenericResponse<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        try{
            RoleDTO newRole = userAndRoleMapper.roleToRoleDto(roleService.createRole(roleDTO));
            return ResponseEntity.ok().body(
                    GenericResponse.<RoleDTO>builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .message("Role created Succesfully!!!")
                            .data(newRole)
                            .build());
        }catch(RoleException re ){
            return ResponseEntity.ok().body(GenericResponse.<RoleDTO>builder()
                    .status(HttpStatus.CONFLICT)
                    .success(false)
                    .message(re.getMessage())
                    .build());
        }
    }

    @PutMapping("${role.put}/{roleId}")
    /*
    When I change roleName of ROLE_ADMIN, then I navigate on another end point also if I use the Bearer token,
    I will not have permission anymore.
    So if it happens, you need to register new admin, after you can go to modify again a roleName.
     */
    public ResponseEntity<GenericResponse<RoleDTO>> updateRole
            (@PathVariable long roleId, @RequestBody RoleDTO roleModifiedDTO) {
        try{
            RoleDTO updatedRole  = userAndRoleMapper.roleToRoleDto(roleService.updateRole(roleId,roleModifiedDTO));
            return ResponseEntity.ok().body(GenericResponse.<RoleDTO>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .message("Role updated Succesfully!!!")
                    .data(updatedRole)
                    .build());
        }catch(RoleException re){
            return ResponseEntity.ok().body(GenericResponse.<RoleDTO>builder()
                    .status(HttpStatus.CONFLICT)
                    .success(false)
                    .message(re.getMessage())
                    .build());
        }catch(ResourceNotFoundException resourseException){
            return ResponseEntity.ok().body(GenericResponse.<RoleDTO>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .success(false)
                    .message(resourseException.getMessage())
                    .build());
        }
    }

    @DeleteMapping("${delete.role}/{roleId}")
    public ResponseEntity<GenericResponse<String>> deleteRole(
            @PathVariable long roleId,
            @RequestParam (name = "deleteUsers" , required = false) boolean deleteUsers) {
        try{
            String roleDeleted = roleService.deleteRole(roleId,deleteUsers);
            return ResponseEntity.ok().body(
                    GenericResponse.<String>builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .message(roleDeleted)
                            .build()
            );
        }catch(ResourceNotFoundException | RoleException re){
            return ResponseEntity.ok().body(
                    GenericResponse.<String>builder()
                            .status(HttpStatus.CONFLICT)
                            .success(false)
                            .message(re.getMessage())
                            .build()
            );
        }
    }
}
