package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Recipe;
import sk.filo.recipes.so.RecipeBasicSO;
import sk.filo.recipes.so.RecipeSO;
import sk.filo.recipes.so.RecipeSimpleSO;
import sk.filo.recipes.so.view.RecipeViewSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = { SimpleCategoryMapper.class, SimpleSourceMapper.class, SimpleRecipeMapper.class, IngredientMapper.class, SimplePictureMapper.class}
)
public interface RecipeMapper {

    @Mappings({
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "modifier", ignore = true),
            @Mapping(target = "modified", ignore = true),
            @Mapping(target = "categories", ignore = true),
            @Mapping(target = "associatedRecipes", ignore = true),
            @Mapping(target = "sections", ignore = true),
            @Mapping(target = "pictures", ignore = true)
    })
    Recipe mapRecipeSOToRecipe(RecipeSO recipeSO);
    
    @Mappings({
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "modifier", ignore = true),
            @Mapping(target = "modified", ignore = true),
            @Mapping(target = "categories", ignore = true),
            @Mapping(target = "associatedRecipes", ignore = true),
            @Mapping(target = "sections", ignore = true),
            @Mapping(target = "pictures", ignore = true)
    })
    void mapRecipeSOToRecipe(RecipeSO recipeSO, @MappingTarget Recipe recipe);
    
    @Mappings({
            @Mapping(target = "creator", source = "creator.username"),
            @Mapping(target = "modifier", source = "modifier.username"),
    })
    RecipeSO mapRecipeToRecipeSO(Recipe recipe);
    
    @Mappings({
            @Mapping(target = "creator", source = "creator.username"),
            @Mapping(target = "modifier", source = "modifier.username"),
    })
    RecipeViewSO mapRecipeToRecipeViewSO(Recipe recipe);
    
    List<RecipeSO> mapRecipeListToRecipeSOList(List<Recipe> recipe);
    
    RecipeBasicSO mapRecipeToRecipeBasicSO(Recipe recipe);
    
    RecipeSimpleSO mapRecipeToRecipeSimpleSO(Recipe recipe);
    
    List<RecipeSimpleSO> mapRecipeListToRecipeSimpleSOList(List<Recipe> recipe);
    
    List<RecipeBasicSO> mapRecipeListToRecipeBasicSOList(List<Recipe> recipe);
}
