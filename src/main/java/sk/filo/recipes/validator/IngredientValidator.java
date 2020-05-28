package sk.filo.recipes.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.domain.Unit;
import sk.filo.recipes.repository.UnitRepository;
import sk.filo.recipes.so.IngredientSO;
import sk.filo.recipes.so.RecipeSO;

/**
 *
 * @author tomas
 */
@Component
public class IngredientValidator implements Validator {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientValidator.class);
    
    @Autowired
    private UnitRepository unitRepository;

    @Override
    public boolean supports(Class clazz) {
        return RecipeSO.class.equals(clazz);
    }
    
    @Override
    public void validate(Object obj, Errors e) {
        RecipeSO recipe = (RecipeSO) obj;
        LOGGER.debug("Validating ingredients {}", recipe);
        for (int i = 0; i < recipe.getSections().size(); i++) {
            for (int j = 0; j < recipe.getSections().get(i).getIngredients().size(); j++) {
                ingredient(recipe.getSections().get(i).getIngredients().get(j), i, j, e);
            }
        }
    }
    
    private void ingredient(IngredientSO so, Integer secIdx, Integer ingIdx, Errors e) {
        if (so!=null && so.getUnitId()!=null) {
            Unit unit = unitRepository.findById(so.getUnitId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit not found!"));
            if (unit.getValueRequired() && so.getValue()==null) {
                e.rejectValue("sections[" + secIdx + "].ingredients[" + ingIdx + "].value", "NotBlank", null);
            }
        }
    }
}
