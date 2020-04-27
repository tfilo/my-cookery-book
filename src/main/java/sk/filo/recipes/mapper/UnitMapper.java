package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Unit;
import sk.filo.recipes.domain.UnitCategory;
import sk.filo.recipes.so.UnitCategorySO;
import sk.filo.recipes.so.UnitSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UnitMapper {
 
    Unit mapUnitSOToUnit(UnitSO unitSO);

    @Mappings({
        @Mapping(source = "category.id", target = "unitCategoryId"),
    })
    UnitSO mapUnitToUnitSO(Unit unit);
    
    UnitCategory mapUnitCategorySOToUnitCategory(UnitCategorySO unitCategorySO);
    
    UnitCategorySO mapUnitCategoryToUnitCategorySO(UnitCategory unitCategory);
    
    List<UnitCategorySO> mapUnitCategoryListToUnitCategorySOList(List<UnitCategory> unitCategories);

    List<UnitSO> mapUnitListToUnitSOList(List<Unit> units);
}
