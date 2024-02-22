package com.fincons.taskmanager.service.authService.impl;

import com.fincons.taskmanager.dto.RoleDTO;
import com.fincons.taskmanager.entity.Role;
import com.fincons.taskmanager.entity.User;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.mapper.UserAndRoleMapper;
import com.fincons.taskmanager.repository.RoleRepository;
import com.fincons.taskmanager.repository.UserRepository;
import com.fincons.taskmanager.service.authService.RoleService;
import com.fincons.taskmanager.utility.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAndRoleMapper userAndRoleMapper;

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(long roleId) {

        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isEmpty()){
            throw new ResourceNotFoundException("Role does not present");
        }
        Role optionalRoleToRole = optionalRole.get();
        optionalRoleToRole.setName(optionalRole.get().getName());
        optionalRoleToRole.setId(optionalRole.get().getId());
        optionalRoleToRole.setUsers(optionalRole.get().getUsers());

        return optionalRoleToRole;
    }

    @Override
    public Role createRole(RoleDTO roleDTO) throws RoleException {

        if(!RoleValidator.isValidRole(roleDTO.getName().toUpperCase())){
            throw new RoleException(RoleException.roleDoesNotRespectRegex());
        }
        Role roleExist = roleRepository.findByName(roleDTO.getName().toUpperCase());

        if(roleExist != null){
            throw new RoleException(RoleException.roleExistException());
        }

        roleDTO.setName(roleDTO.getName().toUpperCase());

        return roleRepository.save(userAndRoleMapper.dtoToRole(roleDTO));
    }

    @Override
    public Role updateRole(long roleId, RoleDTO roleModifiedDTO) throws  RoleException {

        String roleDTONameToConfront = roleModifiedDTO.getName().toUpperCase();

        if(!RoleValidator.isValidRole(roleDTONameToConfront)){
            throw new RoleException(RoleException.roleDoesNotRespectRegex());
        }

        Optional<Role> roleFound = roleRepository.findById(roleId);

        if(roleFound.isEmpty()){
            throw new ResourceNotFoundException("Role does not Exist");
        }

        if(roleFound.get().getName().equals("ROLE_ADMIN")){
            throw new RoleException("Can't modify a ROLE_ADMIN");
        }

        Role roleToModify = roleFound.get();

        roleToModify.setName(roleModifiedDTO.getName().toUpperCase());
        if(roleRepository.findByName(roleModifiedDTO.getName().toUpperCase())!= null) {
            throw new RoleException(RoleException.roleExistException());
        }

        return roleRepository.save(roleToModify);
    }

    @Override
    public String deleteRole(long roleId, boolean deleteUsersAssociated) throws RoleException {  //  if Boolean deleteUsersAssociated == null -- > nullPointerExepciont

        Optional<Role> roleToDelete = Optional.ofNullable(roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role does not Exist")));

        if (roleToDelete.isPresent() && !roleToDelete.get().getName().equals("ROLE_ADMIN"))  {
            if (roleToDelete.get().getUsers().isEmpty()) {
                roleRepository.deleteById(roleId);
                return "Role deleted successfully!";

            } else {
                if (deleteUsersAssociated) {
                    roleToDelete.get().removeUsersAssociations();
                    roleToDelete.get().getUsers()
                            .forEach(user -> userRepository.delete(user));
                    roleRepository.deleteById(roleToDelete.get().getId());
                    return "Role and users associated, deleted!";
                } else {
                    List<Long> idOfUsersToModifyRole = roleToDelete.get()
                            .getUsers()
                            .stream()
                            .map(User::getId)
                            .toList();

                    throw new RoleException("You have to change before the role's name of these users: List of id of users : " + idOfUsersToModifyRole);
                }
            }
        }else{
            throw new RoleException("Can't delete a ROLE_ADMIN");
        }

    }
}
