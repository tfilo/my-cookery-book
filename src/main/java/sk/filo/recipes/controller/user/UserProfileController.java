package sk.filo.recipes.controller.user;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.service.CategoryService;
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
       
    private static final String MODEL_USER_BASIC_SO = "userBasicSO";
    
    private final Validator validator = new PasswordValidator();
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    UserService userService;
    
    private void setAllCategoriesWithRecipes(Model model) {
        model.addAttribute("allCategoriesWithRecipes", categoryService.getFist4RecipesForEveryCategory());
    }
    
    @RequestMapping(value="/profile")
    public String getUsers(final Model model) {
        LOGGER.debug("Get own profile");
        UserBasicSO ownUser = userService.getOwnUser();
        model.addAttribute(MODEL_USER_BASIC_SO, ownUser);
        return "fragments/user :: profilForm";
    }
    
    @RequestMapping(value="/save")
    public String saveUser(final Model model, final @Valid UserBasicSO userBasicSO, final BindingResult bindingResult) {
        LOGGER.debug("Save user profile action {}", userBasicSO);
        validator.validate(userBasicSO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "fragments/user::profilForm";
        }
        userService.saveProfile(userBasicSO);

        setAllCategoriesWithRecipes(model);
        return "fragments/view::categoriesPreview";
    }

}
