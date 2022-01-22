package sk.filo.recipes.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.domain.Role;
import sk.filo.recipes.domain.RoleName;
import sk.filo.recipes.domain.User;
import sk.filo.recipes.mapper.UserMapper;
import sk.filo.recipes.repository.RecipeRepository;
import sk.filo.recipes.repository.RoleRepository;
import sk.filo.recipes.repository.UserRepository;
import sk.filo.recipes.so.UserBasicSO;
import sk.filo.recipes.so.UserSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class UserService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    MessageSource messageSource;
    
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
        user.getRoles().clear();
        
        Set<Role> roles = new HashSet<>();
        Sort sort = Sort.by(Sort.Order.asc("name"));
        userSO.getRoles().forEach((roleName) -> {
            roles.add(roleRepository.findByName(RoleName.valueOf(roleName), sort).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found!")));
        });
        user.getRoles().addAll(roles);
        
        LOGGER.debug("save user {}", user);
        userRepository.save(user);
    }
    
    
    @Transactional
    public void saveProfile(UserBasicSO userBasicSO) {
        LOGGER.debug("save profile (userBasicSO) {}", userBasicSO);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));
        userMapper.mapUserBasicSOToUser(userBasicSO, user);
        if (!StringUtils.isEmptyOrWhitespace(userBasicSO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userBasicSO.getPassword()));
        }

        LOGGER.debug("save user profile {}", user);
        userRepository.save(user);
    }
    
    public void delete(Long id) {
        if (recipeRepository.countByCreatorIdOrModifierId(id, id) == 0) {
            userRepository.deleteById(id);
        } else {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("user.delete.constraint");
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
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
    
    public UserBasicSO getBasicByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));
        return userMapper.mapUserToUserBasicSO(user);
    }
    
    public UserBasicSO getOwnUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authenticatedUser = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));
        return userMapper.mapUserToUserBasicSO(authenticatedUser);
    }
}
