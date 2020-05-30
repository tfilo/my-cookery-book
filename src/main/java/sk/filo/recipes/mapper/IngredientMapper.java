package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
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

    @Mappings({
        @Mapping(target = "value", source="value", qualifiedByName = "FloatToString")
    })
    IngredientViewSO mapIngredientToIngredientViewSO(Ingredient ingredient);
    
    @Named("FloatToString")
    default String floatToString(Float value) {
        if (value % 1.0 != 0) {
            return String.format("%s", value);
        } else {
            return String.format("%.0f", value);
        }
    }
}
