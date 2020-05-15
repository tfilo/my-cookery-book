package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Category;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.CategoryWithRecipeBasicSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CategoryMapper {

    Category mapCategorySOToCategory(CategorySO categorySO);

    CategorySO mapCategoryToCategorySO(Category category);

    List<CategorySO> mapCategoryListToCategorySOList(List<Category> category);
    
    CategoryWithRecipeBasicSO mapCategoryToCategoryWithRecipeBasicSO(Category category);

    List<CategoryWithRecipeBasicSO> mapCategoryListToCategoryWithRecipeBasicSOList(List<Category> category);
}
