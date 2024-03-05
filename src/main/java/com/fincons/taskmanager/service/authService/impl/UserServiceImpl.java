package com.fincons.taskmanager.service.authService.impl;

import com.fincons.taskmanager.dto.LoginDTO;
import com.fincons.taskmanager.dto.UserDTO;
import com.fincons.taskmanager.entity.Role;
import com.fincons.taskmanager.entity.User;
import com.fincons.taskmanager.exception.EmailException;
import com.fincons.taskmanager.exception.PasswordException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.jwt.JwtTokenProvider;
import com.fincons.taskmanager.mapper.UserAndRoleMapper;
import com.fincons.taskmanager.repository.RoleRepository;
import com.fincons.taskmanager.repository.UserRepository;
import com.fincons.taskmanager.service.authService.UserService;
import com.fincons.taskmanager.utility.EmailValidator;
import com.fincons.taskmanager.utility.PasswordValidator;
import com.fincons.taskmanager.utility.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    public UserServiceImpl(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepo;
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAndRoleMapper userAndRoleMapper;
    private JwtTokenProvider jwtTokenProvider;
    @Value("${admin.password}")
    private String passwordAdmin;
    @Value("${editor.password}")
    private String passwordEditor;

    @Override
    public User registerNewUser(UserDTO userDTO, String passwordForAdmin, String passwordForEditor) throws EmailException, PasswordException {
        String emailDto = userDTO.getEmail().toLowerCase().replace(" ", "");
        if (emailDto.isEmpty() || !EmailValidator.isValidEmail(emailDto) || userRepo.existsByEmail(emailDto)) {
            LOG.warn("Invalid or existing email!!");
            throw new EmailException(EmailException.emailInvalidOrExist());
        }
        User userToSave = userAndRoleMapper.dtoToUser(userDTO);
        Role role;
        if (!PasswordValidator.isValidPassword(userDTO.getPassword())) {
            LOG.warn("Password dos not respect Regex!!");
            throw new PasswordException(PasswordException.passwordDoesNotRespectRegexException());
        }
        if (passwordAdmin.equals(passwordForAdmin)) {
            role = roleToAssign("ROLE_ADMIN");
        } else if (passwordEditor.equals(passwordForEditor)) {
            role = roleToAssign("ROLE_EDITOR");
        } else {
            role = roleToAssign("ROLE_USER");
        }
        userToSave.setRoles(List.of(role));
        return userRepo.save(userToSave);
    }

    @Override
    public String login(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public User updateUser(String email, UserDTO userModified, String passwordForAdmin) throws PasswordException, RoleException {
        if (email.isEmpty() && !EmailValidator.isValidEmail(email)) {
            throw new ResourceNotFoundException("Invalid email!");
        }
        User userFound = userRepo.findByEmail(email);
        if (userFound == null) {
            throw new ResourceNotFoundException("There isn't an user with this email!");
        }
        userFound.setFirstName(userModified.getFirstName());
        userFound.setLastName(userModified.getLastName());
        if (!PasswordValidator.isValidPassword(userModified.getPassword())) {
            throw new PasswordException(PasswordException.passwordDoesNotRespectRegexException());
        }
        userFound.setPassword(passwordEncoder.encode(userModified.getPassword()));
        if (passwordForAdmin != null && passwordForAdmin.equals(passwordAdmin)) {
            if (RoleValidator.isValidRole(String.valueOf(userModified.getRoles().get(0))))
                throw new RoleException(RoleException.roleDoesNotRespectRegex());
            userFound.setRoles(userModified.getRoles()
                    .stream()
                    .map(role -> userAndRoleMapper.dtoToRole(role))
                    .collect(Collectors.toList()));
        }
        return userRepo.save(userFound);
    }

    @Override
    public User updateUserPassword(String email, String currentPassword, String newPassword) throws EmailException, PasswordException {
        if (userRepo.findByEmail(email) == null || !EmailValidator.isValidEmail(email)) {
            throw new EmailException(EmailException.emailInvalidOrExist());
        }
        User userToModify = userRepo.findByEmail(email);
        boolean passwordMatch = passwordEncoder.matches(currentPassword, userToModify.getPassword());
        if (!passwordMatch) {
            throw new PasswordException(PasswordException.invalidPasswordException());
        }
        if (!PasswordValidator.isValidPassword(newPassword)) {
            throw new PasswordException(PasswordException.passwordDoesNotRespectRegexException());
        }
        userToModify.setPassword(passwordEncoder.encode(newPassword));
        return userRepo.save(userToModify);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void deleteUserByEmail(String email) throws EmailException, RoleException {
        User userToRemove = userRepo.findByEmail(email);
        if (userToRemove == null) {
            throw new EmailException(EmailException.emailInvalidOrExist());
        }
        if (userToRemove.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new RoleException("User with role admin can't be deleted!");
        }
        userRepo.delete(userToRemove);
    }

    @Override
    public User getUserDetails() {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }
        return userRepo.findByEmail(loggedUser);
    }

    public Role roleToAssign(String nomeRuolo) {
        Role role = roleRepo.findByName(nomeRuolo);
        if (role == null) {
            Role newRole = new Role();
            newRole.setName(nomeRuolo);
            role = roleRepo.save(newRole);
        }
        return role;
    }
}
