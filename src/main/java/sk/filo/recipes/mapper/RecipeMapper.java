package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Recipe;
import sk.filo.recipes.so.RecipeSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = { SimpleCategoryMapper.class, SimpleRecipeMapper.class}
)
public interface RecipeMapper {

    @Mappings({
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "modifier", ignore = true),
            @Mapping(target = "modified", ignore = true),
            @Mapping(target = "categories", ignore = true),
            @Mapping(target = "associatedRecipes", ignore = true)
    })
    Recipe mapRecipeSOToRecipe(RecipeSO recipeSO);

    @Mappings({
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "modifier", ignore = true),
            @Mapping(target = "modified", ignore = true),
            @Mapping(target = "categories", ignore = true),
            @Mapping(target = "associatedRecipes", ignore = true)
    })
    void mapRecipeSOToRecipe(RecipeSO recipeSO, @MappingTarget Recipe recipe);
    
    @Mappings({
            @Mapping(target = "creator", source = "creator.username"),
            @Mapping(target = "modifier", source = "modifier.username"),
    })
    RecipeSO mapRecipeToRecipeSO(Recipe recipe);
    
    List<RecipeSO> mapRecipeListToRecipeSOList(List<Recipe> recipe);
}
