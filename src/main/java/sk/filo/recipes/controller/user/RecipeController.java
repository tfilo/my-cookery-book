package sk.filo.recipes.controller.user;

import java.util.ArrayList;
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
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.service.UnitCategoryService;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.IngredientSO;
import sk.filo.recipes.so.RecipeSimpleSO;
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
    
    private static final String MODEL_CATEGORIES = "allCategories";
    
    private static final String MODEL_UNIT_CATEGORIES_WITH_UNITS = "allUnitCategoriesWithUnits";

    private static final String MODEL_FILTERED_RECIPES = "filteredRecipes";
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    UnitCategoryService unitCategoryService;
    
    private void setAllCategoriesWithRecipes(Model model) {
        model.addAttribute("allCategoriesWithRecipes", categoryService.getFist4RecipesForEveryCategory());
    }
    
    @ModelAttribute(MODEL_CATEGORIES)
    public List<CategorySO> allCategories() {
        LOGGER.debug("allCategories");
        return categoryService.getAll();
    }
    
    @ModelAttribute(MODEL_UNIT_CATEGORIES_WITH_UNITS)
    public List<UnitCategorySO> allUnitCategoriesWithUnits() {
        LOGGER.debug("allUnitCategories");
        return unitCategoryService.getAll();
    }
    
    @RequestMapping(value="/add")
    public String addRecipe(final Model model, final HttpServletRequest req) {
        LOGGER.debug("Add recipe");
        RecipeSO recipeSO = new RecipeSO();
        recipeSO.getSections().add(new SectionSO());
        recipeSO.setCreator(req.getUserPrincipal().getName());
        model.addAttribute(MODEL_RECIPE_SO, recipeSO);
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value="/edit/{recipeId}")
    public String editRecipe(final Model model, @PathVariable Long recipeId, final HttpServletRequest req) {
        LOGGER.debug("Edit recipe by id {}", recipeId);
        RecipeSO recipeSO = recipeService.get(recipeId);
        LOGGER.debug("Loaded recipe {}", recipeSO);
        model.addAttribute(MODEL_RECIPE_SO, recipeSO);
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value = "/sourceAdd")
    public String addSource(final RecipeSO recipeSO) {
        LOGGER.debug("addRow source {}", recipeSO);
        if (recipeSO!=null) {
            recipeSO.getSources().add(new SourceSO());
        }
        return "fragments/recipe::recipeForm";
    }

    @RequestMapping(value = "/sourceRemove/{rowId}")
    public String removeSource(final RecipeSO recipeSO, @PathVariable Integer rowId , HttpServletRequest req) {
        LOGGER.debug("removeRow source {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            recipeSO.getSources().remove(rowId.intValue());
        }
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value = "/associatedRecipe/add/{recipeId}")
    public String addAssociatedRecipe(final RecipeSO recipeSO, @PathVariable Long recipeId) {
        LOGGER.debug("addAssociatedRecipe {}, {}", recipeSO, recipeId);
        if (recipeId!=null && recipeSO!=null) {
            RecipeSimpleSO so = recipeService.getBasic(recipeId);
            if (!recipeSO.getAssociatedRecipes().contains(so)) {
                recipeSO.getAssociatedRecipes().add(so);
            }
        }
        return "fragments/recipe::associatedRecipes";
    }

    @RequestMapping(value = "/associatedRecipe/remove/{rowId}")
    public String removeAssociatedRecipe(final RecipeSO recipeSO, @PathVariable Integer rowId) {
        LOGGER.debug("removeAssociatedRecipe {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            recipeSO.getAssociatedRecipes().remove(rowId.intValue());
        }
        return "fragments/recipe::associatedRecipes";
    }
    
    @RequestMapping(value = "/sectionAdd")
    public String addSection(final RecipeSO recipeSO) {
        LOGGER.debug("addRow section {}", recipeSO);
        if (recipeSO!=null) {
            recipeSO.getSections().add(new SectionSO());
        }
        return "fragments/recipe::sections";
    }

    @RequestMapping(value = "/sectionRemove/{rowId}")
    public String removeSection(final RecipeSO recipeSO, @PathVariable Integer rowId) {
        LOGGER.debug("removeRow section {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            if (recipeSO.getSections().size() > 1) {
                recipeSO.getSections().remove(rowId.intValue());
            }
        }
        return "fragments/recipe::sections";
    }

    @RequestMapping(value = "/ingredientAdd/{sectionRowId}")
    public String addIngredient(final RecipeSO recipeSO, @PathVariable Integer sectionRowId) {
        LOGGER.debug("addRow ingredient {}, {}", recipeSO, sectionRowId);
        if (recipeSO!=null) {
            recipeSO.getSections().get(sectionRowId).getIngredients().add(new IngredientSO());
        }
        LOGGER.debug("new ingredient {}", recipeSO);
        return "fragments/recipe::sections";
    }

    @RequestMapping(value = "/ingredientRemove/{sectionRowId}/{rowId}")
    public String removeIngredient(final RecipeSO recipeSO, @PathVariable Integer sectionRowId, @PathVariable Integer rowId , HttpServletRequest req) {
        LOGGER.debug("removeRow ingredient {}, {}, {}", recipeSO, sectionRowId, rowId);
        if (recipeSO != null) {
            recipeSO.getSections().get(sectionRowId).getIngredients().remove(rowId.intValue());
        }
        return "fragments/recipe::sections";
    }
    
    @RequestMapping(value="/delete")
    public String deleteRecipe(final Model model, final RecipeSO recipe) {
        LOGGER.debug("Delete recipe action {}", recipe);
        recipeService.delete(recipe.getId());
        setAllCategoriesWithRecipes(model);
        return "fragments/view::categoriesPreview";
    }
    
    @RequestMapping(value="/save")
    public String saveRecipe(final Model model, final @Valid RecipeSO recipe, final BindingResult bindingResult) {
        LOGGER.debug("Save recipe action {}", recipe);
        if (bindingResult.hasErrors()) {
            LOGGER.debug(bindingResult.toString());
            return "fragments/recipe::recipeForm";
        }
        recipeService.save(recipe);
        setAllCategoriesWithRecipes(model);
        return "fragments/view::categoriesPreview";
    }
    
    @RequestMapping(value="/filter")
    public String filterRecipes(final Model model, final RecipeSO recipeSO, final String title) {
        LOGGER.debug("filterRecipes {}, {}", title, recipeSO);
        List<RecipeSimpleSO> filtered;
        if (StringUtils.isEmptyOrWhitespace(title)) {
            filtered = new ArrayList<>();
        } else {
            filtered = recipeService.findRecipeSimpleByTitle(title);   
        }
        model.addAttribute(MODEL_FILTERED_RECIPES, filtered);
        return "fragments/recipe::filteredRecipes";
    }
}
