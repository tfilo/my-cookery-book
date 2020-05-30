package sk.filo.recipes.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.component.Preview;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.so.CategorySO;

/**
 *
 * @author tomas
 */
@Controller
public class HomeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    private static final String MODEL_CATEGORIES = "allCategories";
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    Preview preview;

    @ModelAttribute(MODEL_CATEGORIES)
    public List<CategorySO> allCategories() {
        LOGGER.debug("allCategories");
        return categoryService.getAll();
    }
    
    // Login form
    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }

    // Login form with error
    @RequestMapping("/login-error.html")
    public String loginError(final Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping({"/", "/"})
    public String formPage(final Model model) {
        LOGGER.debug("Getting Home page");
        preview.setAllCategoriesWithRecipes(model);
        return "home";
    }
}
