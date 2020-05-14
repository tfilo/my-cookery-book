package sk.filo.recipes.mapper;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Category;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public class SimpleCategoryMapper {

    public Long mapCategoryToCategoryId(Category category) {
        return category != null ? category.getId() : null;
    }

    public List<Long> mapCategoryListToCategoryIdList(List<Category> categories) {
        if (categories != null) {
            final List<Long> result = new ArrayList<>();
            categories.forEach(category -> {
                result.add(mapCategoryToCategoryId(category));
            });
            return result;
        }
        return null;
    }
}
