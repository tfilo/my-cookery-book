package sk.filo.recipes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author tomas
 */
@Controller
public class RecipeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);
    
    @RequestMapping(value="/recipe/add")
    public String addRecipe(final Model model) {
        LOGGER.debug("Add recipe");
        model.addAttribute("recipe", "new recipe");
        return "home";
    }
}
