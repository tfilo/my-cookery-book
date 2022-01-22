package sk.filo.recipes.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.component.Search;
import sk.filo.recipes.controller.ModelAttributeConstants;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.service.UserService;
import sk.filo.recipes.so.UserBasicSO;
import sk.filo.recipes.validator.PasswordValidator;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/user")
public class UserProfileController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);
 
    @Autowired
    PasswordValidator passwordValidator;
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    Search search;

    @RequestMapping(value="/profile")
    public String getUsers(final Model model) {
        LOGGER.debug("Get own profile");
        UserBasicSO ownUser = userService.getOwnUser();
        model.addAttribute(ModelAttributeConstants.MODEL_USER_BASIC_SO, ownUser);
        return "fragments/user :: profilForm";
    }
    
    @RequestMapping(value="/save")
    public String saveUser(final Model model, final @Valid UserBasicSO userBasicSO, final BindingResult bindingResult, final HttpServletRequest req) {
        LOGGER.debug("Save user profile action {}", userBasicSO);
        passwordValidator.validate(userBasicSO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "fragments/user::profilForm";
        }
        userService.saveProfile(userBasicSO);

        return search.backToCategory(model, req);
    }

}
