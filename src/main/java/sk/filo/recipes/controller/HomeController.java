package sk.filo.recipes.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.so.CategoryWithRecipeBasicSO;

/**
 *
 * @author tomas
 */
@Controller
public class HomeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    CategoryService categoryService;
    
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ModelAttribute("allCategories")
    public List<CategoryWithRecipeBasicSO> allCategories() {
        return categoryService.getFist4RecipesForEveryCategory();
    }
    
    @RequestMapping({"/", "/"})
    public String formPage(final Model model) {
        LOGGER.debug("Getting Home page");
        return "home";
    }
    
}
