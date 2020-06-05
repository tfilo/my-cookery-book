package sk.filo.recipes.controller.admin;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.controller.ModelAttributeConstants;
import sk.filo.recipes.service.UserService;
import sk.filo.recipes.so.UserSO;
import sk.filo.recipes.validator.PasswordValidator;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    private final Validator validator = new PasswordValidator();
    
    @Autowired
    UserService userService;
    
    private void setAllUsers(final Model model) {
        List<UserSO> all = userService.getAll();
        LOGGER.debug("setAllUsers {}", all);
        model.addAttribute(ModelAttributeConstants.MODEL_USERS, all);
    }
    
    @RequestMapping(value="/all")
    public String getUsers(final Model model) {
        LOGGER.debug("Get all users");
        setAllUsers(model);
        return "fragments/user :: usersList";
    }
    
    @RequestMapping(value="/add")
    public String addUser(final Model model) {
        LOGGER.debug("Create user action");
        model.addAttribute(ModelAttributeConstants.MODEL_USER_SO, new UserSO());
        return "fragments/user::userForm";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getUserById(final @PathVariable Long id, final Model model) {
        LOGGER.debug("Get user by id {}", id);
        UserSO user = userService.get(id);
        model.addAttribute(ModelAttributeConstants.MODEL_USER_SO, user);
        return "fragments/user::userForm";
    }
    
    @RequestMapping(value="/save")
    public String saveUser(final Model model, final @Valid UserSO userSO, final BindingResult bindingResult) {
        LOGGER.debug("Save user action {}", userSO);
        validator.validate(userSO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "fragments/user::userForm";
        }
        userService.save(userSO);
        setAllUsers(model);
        LOGGER.debug("Uzivatelia po ulozeni: {}",model.getAttribute(ModelAttributeConstants.MODEL_USERS));
        return "fragments/user::usersList";
    }
    
    @RequestMapping(value="/delete/{userId}")
    public String deleteUser(final Model model, final @PathVariable Long userId) {
        LOGGER.debug("Delete user action {}", userId);
        userService.delete(userId);
        setAllUsers(model);
        return "fragments/user::usersList";
    }
}
