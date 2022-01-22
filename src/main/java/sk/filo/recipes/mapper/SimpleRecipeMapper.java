package sk.filo.recipes.mapper;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Recipe;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public class SimpleRecipeMapper {

    public Long mapRecipeToRecipeId(Recipe recipe) {
        return recipe != null ? recipe.getId() : null;
    }

    public List<Long> mapRecipeListToRecipeIdList(List<Recipe> recipes) {
        if (recipes != null) {
            final List<Long> result = new ArrayList<>();
            recipes.forEach(recipe -> {
                result.add(mapRecipeToRecipeId(recipe));
            });
            return result;
        }
        return null;
    }
}
