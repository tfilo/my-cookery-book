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
import sk.filo.recipes.component.Preview;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.RecipeSearchCriteriaSO;

/**
 *
 * @author tomas
 */
@Controller
public class HomeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    private static final String MODEL_CATEGORIES = "allCategories";
    
    private static final String USED_FRAGMENT = "usedFragment";
    
    private static final String RECIPE_ID = "recipeId";
    private static final String CATEGORY_ID = "categoryId";
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    Preview preview;
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    RecipeViewController recipeViewController;

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
    
    // Cookies info
    @RequestMapping("/cookies")
    public String cookies() {
        return "cookies";
    }

    @RequestMapping({"/", "/recipe/{recipeId}", "/category/{categoryId}"})
    public String formPage(final Model model, @PathVariable Optional<Long> recipeId, @PathVariable Optional<Long> categoryId, final HttpServletRequest req) {
        LOGGER.debug("Getting Home page");
        
        HttpSession session = req.getSession();
        
        if (recipeId.isEmpty() && categoryId.isEmpty()) {
            if (session.getAttribute(RECIPE_ID)==null && session.getAttribute(CATEGORY_ID)==null) {
                preview.setAllCategoriesWithRecipes(model);
                model.addAttribute(USED_FRAGMENT, "view :: recipesList");
            }
            
            if (session.getAttribute(RECIPE_ID)!=null) {
                recipeViewController.viewRecipe(model, (Long)session.getAttribute(RECIPE_ID), req);
                model.addAttribute(USED_FRAGMENT, "view :: recipeView");
            }
            
            if (session.getAttribute(CATEGORY_ID)!=null) {
                RecipeSearchCriteriaSO so = new RecipeSearchCriteriaSO();
                so.setCategoryId((Long)session.getAttribute(CATEGORY_ID));
                recipeViewController.viewRecipesInCategory(model, so, req);
                model.addAttribute(USED_FRAGMENT, "view :: recipesList");
            }

            session.setAttribute(RECIPE_ID, null);
            session.setAttribute(CATEGORY_ID, null);
            return "home";
        }      
        
        if (recipeId.isPresent()) {
            session.setAttribute(RECIPE_ID, recipeId.get());
            session.setAttribute(CATEGORY_ID, null);
        }
        
        if (categoryId.isPresent()) {
            session.setAttribute(RECIPE_ID, null);
            session.setAttribute(CATEGORY_ID, categoryId.get());
        }
        
        return "redirect:/";
    }
}
