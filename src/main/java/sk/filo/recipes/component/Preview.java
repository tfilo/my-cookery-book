package sk.filo.recipes.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.so.RecipeSearchCriteriaSO;

/**
 *
 * @author tomas
 */
@Component
public class Preview {
    
    private static final String TITLE = "title";
    
    private static final String MODEL_RECIPES = "recipes";
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    MessageSource messageSource;
    
    public void setAllCategoriesWithRecipes(Model model) {
        RecipeSearchCriteriaSO criteria = new RecipeSearchCriteriaSO();
        criteria.setPage(0);
        criteria.setPageSize(16);
        criteria.setDirection(RecipeSearchCriteriaSO.Direction.DESC);
        criteria.setSortField(RecipeSearchCriteriaSO.SortField.created);
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        String message = accessor.getMessage("recipe.preview");
        model.addAttribute(TITLE, message);
        model.addAttribute(MODEL_RECIPES, recipeService.getAllBasicByCriteria(criteria));
    }
}
