package sk.filo.recipes.controller;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.component.Search;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.service.TagService;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.TagSO;

/**
 *
 * @author tomas
 */
@Controller
public class HomeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    Search search;
    
    @Autowired
    TagService tagService;
    
    @Autowired
    RecipeViewController recipeViewController;

    @ModelAttribute(ModelAttributeConstants.MODEL_CATEGORIES)
    public List<CategorySO> Categories() {
        LOGGER.debug("categories");
        return categoryService.getAll();
    }
    
    @ModelAttribute(ModelAttributeConstants.MODEL_TAGS)
    public List<TagSO> Tags() {
        LOGGER.debug("tags");
        return tagService.getAll();
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
    
    // Cookies info
    @RequestMapping("/cookies")
    public String cookies() {
        return "cookies";
    }

    @RequestMapping("/")
    public String formPage(final Model model, final HttpServletRequest req) {
        LOGGER.debug("Getting Home page");

        HttpSession session = req.getSession();

        if (session.getAttribute(ModelAttributeConstants.RECIPE_ID) == null) {
            search.showPreview(model, req);
            model.addAttribute(ModelAttributeConstants.USED_FRAGMENT, "view :: recipesList");
        } else {
            recipeViewController.viewRecipe(model, (Long) session.getAttribute(ModelAttributeConstants.RECIPE_ID), null, req);
            model.addAttribute(ModelAttributeConstants.USED_FRAGMENT, "view :: recipeView");
        }

        session.setAttribute(ModelAttributeConstants.RECIPE_ID, null);

        return "home";
    }

    @RequestMapping("/link/recipe/{recipeId}")
    public String saveLinkToSession(@PathVariable Optional<Long> recipeId, final HttpServletRequest req) {
        LOGGER.debug("Saving recipe link to session");
        
        HttpSession session = req.getSession();    
        
        if (recipeId.isPresent()) {
            session.setAttribute(ModelAttributeConstants.RECIPE_ID, recipeId.get());
        }
        
        return "redirect:/";
    }
}
