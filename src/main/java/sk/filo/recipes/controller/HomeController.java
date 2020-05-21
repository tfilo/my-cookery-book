package sk.filo.recipes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.service.CategoryService;

/**
 *
 * @author tomas
 */
@Controller
public class HomeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    CategoryService categoryService;
    
    private void setAllCategoriesWithRecipes(Model model) {
        model.addAttribute("allCategoriesWithRecipes", categoryService.getFist4RecipesForEveryCategory());
    }
    
    // Login form
    @RequestMapping("/login.html")
    public String login() {
        return "login.html";
    }

    // Login form with error
    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @RequestMapping({"/", "/"})
    public String formPage(final Model model) {
        LOGGER.debug("Getting Home page");
        setAllCategoriesWithRecipes(model);
        return "home";
    }
}
