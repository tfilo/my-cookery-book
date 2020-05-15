package sk.filo.recipes.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.service.UnitCategoryService;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.IngredientSO;
import sk.filo.recipes.so.RecipeBasicSO;
import sk.filo.recipes.so.RecipeSO;
import sk.filo.recipes.so.SectionSO;
import sk.filo.recipes.so.SourceSO;
import sk.filo.recipes.so.UnitCategorySO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/recipe")
public class RecipeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);
    
    private static final String MODEL_RECIPE_SO = "recipeSO";
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    UnitCategoryService unitCategoryService;
    
    @ModelAttribute("allCategories")
    public List<CategorySO> allCategories() {
        return categoryService.getAll();
    }
    
    @ModelAttribute("allUnitCategoriesWithUnits")
    public List<UnitCategorySO> allUnitCategories() {
        return unitCategoryService.getAll();
    }
    
    @ModelAttribute("allRecipes")
    public List<RecipeBasicSO> allRecipes() {
        return recipeService.getAllBasic();
    }
    
    @RequestMapping(value="/add")
    public String addRecipe(final Model model, final HttpServletRequest req) {
        LOGGER.debug("Add recipe");
        RecipeSO recipeSO = new RecipeSO();
        recipeSO.getSections().add(new SectionSO());
        recipeSO.setCreator(req.getUserPrincipal().getName());
        model.addAttribute(MODEL_RECIPE_SO, recipeSO);
        return "recipe";
    }
    
    @RequestMapping(value="/edit/{recipeId}")
    public String editRecipe(final Model model, @PathVariable Long recipeId, final HttpServletRequest req) {
        LOGGER.debug("Edit recipe by id {}", recipeId);
        RecipeSO recipeSO = recipeService.get(recipeId);
        LOGGER.debug("Loaded recipe {}", recipeSO);
        model.addAttribute(MODEL_RECIPE_SO, recipeSO);
        return "recipe";
    }
    
    @RequestMapping(value = "/sourceAdd")
    public String addSource(final RecipeSO recipeSO) {
        LOGGER.debug("addRow source {}", recipeSO);
        if (recipeSO!=null) {
            recipeSO.getSources().add(new SourceSO());
        }
        return "recipe::recipeForm";
    }

    @RequestMapping(value = "/sourceRemove/{rowId}")
    public String removeSource(final RecipeSO recipeSO, @PathVariable Integer rowId , HttpServletRequest req) {
        LOGGER.debug("removeRow source {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            recipeSO.getSources().remove(rowId.intValue());
        }
        return "recipe::recipeForm";
    }
    
    @RequestMapping(value = "/sectionAdd")
    public String addSection(final RecipeSO recipeSO) {
        LOGGER.debug("addRow section {}", recipeSO);
        if (recipeSO!=null) {
            recipeSO.getSections().add(new SectionSO());
        }
        return "recipe::recipeForm";
    }

    @RequestMapping(value = "/sectionRemove/{rowId}")
    public String removeSection(final RecipeSO recipeSO, @PathVariable Integer rowId , HttpServletRequest req) {
        LOGGER.debug("removeRow section {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            if (recipeSO.getSections().size() > 1) {
                recipeSO.getSections().remove(rowId.intValue());
            }
        }
        return "recipe::recipeForm";
    }

    @RequestMapping(value = "/ingredientAdd/{sectionRowId}")
    public String addIngredient(final RecipeSO recipeSO, @PathVariable Integer sectionRowId) {
        LOGGER.debug("addRow ingredient {}, {}", recipeSO, sectionRowId);
        if (recipeSO!=null) {
            recipeSO.getSections().get(sectionRowId).getIngredients().add(new IngredientSO());
        }
        LOGGER.debug("new ingredient {}", recipeSO);
        return "recipe::recipeForm";
    }

    @RequestMapping(value = "/ingredientRemove/{sectionRowId}/{rowId}")
    public String removeIngredient(final RecipeSO recipeSO, @PathVariable Integer sectionRowId, @PathVariable Integer rowId , HttpServletRequest req) {
        LOGGER.debug("removeRow ingredient {}, {}, {}", recipeSO, sectionRowId, rowId);
        if (recipeSO != null) {
            recipeSO.getSections().get(sectionRowId).getIngredients().remove(rowId.intValue());
        }
        return "recipe::recipeForm";
    }
    
    @RequestMapping(value="/delete")
    public String deleteRecipe(final RecipeSO recipe) {
        LOGGER.debug("Delete recipe action {}", recipe);
        recipeService.delete(recipe.getId());
        return "recipe::recipeForm";
    }
    
    @RequestMapping(value="/save")
    public String saveRecipe(final @Valid RecipeSO recipe, final BindingResult bindingResult) {
        LOGGER.debug("Save recipe action {}", recipe);
        if (bindingResult.hasErrors()) {
            return "recipe::recipeForm";
        }
        recipeService.save(recipe);
        return "recipe::recipeForm";
    }
}
