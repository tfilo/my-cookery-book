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
import sk.filo.recipes.so.CategorySO;

/**
 *
 * @author tomas
 */
@Controller
public class HomeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    CategoryService categoryService;
    
    @ModelAttribute("allCategories")
    public List<CategorySO> allCategories() {
        return categoryService.getAll();
    }
    
    @RequestMapping({"/", "/"})
    public String formPage(final Model model) {
        LOGGER.debug("Getting Home page");
        return "home";
    }
    
}
