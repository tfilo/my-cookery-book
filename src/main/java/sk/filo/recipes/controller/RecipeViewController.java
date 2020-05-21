package sk.filo.recipes.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.PictureService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.so.PictureSO;
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
    
    private static final String CATEGORY_ID = "categoryId";
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    PictureService pictureService;
    
    private void setAllCategoriesWithRecipes(Model model) {
        model.addAttribute(MODEL_CATEGORIES_WITH_RECIPES_SO, categoryService.getFist4RecipesForEveryCategory());
    }
    
    private void setAllCategoriesWithRecipesByTitle(Model model, String title) {
        Integer page = 0;
        Integer size = Integer.MAX_VALUE;
        PageRequest pr =  PageRequest.of(page, size, Direction.ASC, "title");
        model.addAttribute(MODEL_CATEGORIES_WITH_RECIPES_SO, categoryService.getRecipesForEveryCategoryByTitle(pr, title, pr));
    }
    
    @RequestMapping(value="recipe/{recipeId}")
    public String viewRecipe(final Model model, final @PathVariable Long recipeId, final HttpServletRequest req) {
        LOGGER.debug("View recipe by id {}", recipeId);
        RecipeViewSO recipeSO = recipeService.getView(recipeId);
        LOGGER.debug("Loaded recipe view {}", recipeSO);
        model.addAttribute(MODEL_RECIPE_VIEW_SO, recipeSO);
        return "fragments/view::recipeView";
    }
    
    @RequestMapping(value="/categoriesPreview")
    public String viewRecipeCategoriesPreview(final Model model, String value) {
        LOGGER.debug("Getting categoriesPreview");
        if (value!=null) {
            setAllCategoriesWithRecipesByTitle(model, value);
        } else {
            setAllCategoriesWithRecipes(model);
        }
        return "fragments/view::categoriesPreview";
    }

    @RequestMapping(value="/recipesByCategory/{categoryId}")
    public String viewRecipesInCategory(final Model model, final @PathVariable Long categoryId, String value) {
        LOGGER.debug("Getting recipes by category");
        Integer page = 0;
        Integer size = Integer.MAX_VALUE; // TODO paginacia
        PageRequest pr =  PageRequest.of(page, size, Direction.ASC, "title");

        if (value!=null) {
            model.addAttribute(MODEL_RECIPES, recipeService.getAllBasicByCategoryIdAndTitle(pr, categoryId, value));
            model.addAttribute(TITLE, "Recipes found for '" + value + "' in category " + categoryService.get(categoryId).getName());
        } else {
            model.addAttribute(MODEL_RECIPES, recipeService.getAllBasicByCategoryId(pr, categoryId));
            model.addAttribute(TITLE, categoryService.get(categoryId).getName());
        }
        
        model.addAttribute(CATEGORY_ID, categoryId);
        
        return "fragments/view::recipesList";
    }
        
    @RequestMapping(value = "/picture/thumbnail/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPictureById(final @PathVariable Long id) {
        LOGGER.debug("Getting picture by id");
        PictureSO pictureSO= pictureService.getThumbnail(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
            
        return new ResponseEntity<>(pictureSO.getData(), headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/picture/thumbnail/byrecipe/{recipeId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPictureThumbnailByRecipeId(final @PathVariable Long recipeId) {
        LOGGER.debug("Getting picture thumbnail by recipeId");
        PictureSO pictureSO= pictureService.getThumbnailByRecipeId(recipeId);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
            
        return new ResponseEntity<>(pictureSO.getData(), headers, HttpStatus.CREATED);
    }
    
}
