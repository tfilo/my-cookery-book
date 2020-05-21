package sk.filo.recipes.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.domain.Role;
import sk.filo.recipes.domain.RoleName;
import sk.filo.recipes.domain.User;
import sk.filo.recipes.mapper.UserMapper;
import sk.filo.recipes.repository.RoleRepository;
import sk.filo.recipes.repository.UserRepository;
import sk.filo.recipes.so.UserSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class UserService {

    private UserMapper userMapper;
    
    private UserRepository userRepository;
    
    private RoleRepository roleRepository;
    
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void save(UserSO userSO) {
        LOGGER.debug("save userSO {}", userSO);
        User user;
        if (Objects.isNull(userSO.getId())) {
            user = userMapper.mapUserSOToUser(userSO);
            user.setPassword(passwordEncoder.encode(userSO.getPassword()));
        } else {
            user = userRepository.findById(userSO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));
            userMapper.mapUserSOToUser(userSO, user);
            if (!StringUtils.isEmptyOrWhitespace(userSO.getPassword())) {
                user.setPassword(passwordEncoder.encode(userSO.getPassword()));
            }
        }
        Set<Role> roles = new HashSet<>();
        Sort sort = Sort.by(Sort.Order.asc("name"));
        userSO.getRoles().forEach((roleName) -> {
            roles.add(roleRepository.findByName(RoleName.valueOf(roleName), sort).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found!")));
        });
        user.setRoles(roles);
        LOGGER.debug("save user {}", user);
        userRepository.save(user);
    }
    
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    
    public List<UserSO> getAll() {
        Sort sort = Sort.by(Sort.Order.asc("lastName"), Sort.Order.asc("firstName"), Sort.Order.asc("username"));
        List<User> allUsers = userRepository.findAll(sort);
        return userMapper.mapUserListToUserSOList(allUsers);
    }
    
    public UserSO get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));
        return userMapper.mapUserToUserSO(user);
    }
}
