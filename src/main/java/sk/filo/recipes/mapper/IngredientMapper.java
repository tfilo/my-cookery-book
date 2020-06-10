package sk.filo.recipes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Ingredient;
import sk.filo.recipes.so.IngredientSO;
import sk.filo.recipes.so.view.IngredientViewSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IngredientMapper {

    @Mappings({
        @Mapping(target = "unit", ignore = true)
    })
    Ingredient mapIngredientSOToIngredient(IngredientSO ingredientSO);
    
    @Mappings({
        @Mapping(target = "unitId", source="unit.id")
    })
    IngredientSO mapIngredientToIngredientSO(Ingredient ingredient);
    
    IngredientViewSO mapIngredientToIngredientViewSO(Ingredient ingredient);
}
