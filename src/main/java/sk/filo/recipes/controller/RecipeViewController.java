package sk.filo.recipes.controller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.so.view.RecipeViewSO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/view")
public class RecipeViewController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeViewController.class);
    
    private static final String MODEL_RECIPE_VIEW_SO = "recipeViewSO";
    
    private static final String MODEL_CATEGORIES_WITH_RECIPES_SO = "allCategoriesWithRecipes";
    
    private static final String MODEL_RECIPES = "recipes";
    
    private static final String TITLE = "title";
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    CategoryService categoryService;
    
    private void setAllCategoriesWithRecipes(Model model) {
        model.addAttribute(MODEL_CATEGORIES_WITH_RECIPES_SO, categoryService.getFist4RecipesForEveryCategory());
    }
    
    @RequestMapping(value="recipe/{recipeId}")
    public String viewRecipe(final Model model, @PathVariable Long recipeId, final HttpServletRequest req) {
        LOGGER.debug("View recipe by id {}", recipeId);
        RecipeViewSO recipeSO = recipeService.getView(recipeId);
        LOGGER.debug("Loaded recipe view {}", recipeSO);
        model.addAttribute(MODEL_RECIPE_VIEW_SO, recipeSO);
        return "fragments/view::recipeView";
    }
    
    @RequestMapping(value="/categoriesPreview")
    public String viewRecipeCategoriesPreview(final Model model) {
        LOGGER.debug("Getting categoriesPreview");
        setAllCategoriesWithRecipes(model);
        return "fragments/view::categoriesPreview";
    }

    @RequestMapping(value="/recipesByCategory/{categoryId}")
    public String viewRecipesInCategory(final Model model, @PathVariable Long categoryId) {
        LOGGER.debug("Getting recipes by category");
        Integer page = 0;
        Integer size = Integer.MAX_VALUE; // TODO paginacia
        PageRequest pr =  PageRequest.of(page, size);
        model.addAttribute(MODEL_RECIPES, recipeService.getAllBasicByCategoryId(pr, categoryId));
        model.addAttribute(TITLE, categoryService.get(categoryId).getName());
        return "fragments/view::recipesList";
    }
    
}
