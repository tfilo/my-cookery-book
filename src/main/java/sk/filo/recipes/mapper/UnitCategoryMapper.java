package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.UnitCategory;
import sk.filo.recipes.so.UnitCategoryBasicSO;
import sk.filo.recipes.so.UnitCategorySO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UnitCategoryMapper {
 
    UnitCategory mapUnitCategorySOToUnitCategory(UnitCategorySO unitCategorySO);
    
    UnitCategory mapUnitCategoryBasicSOToUnitCategory(UnitCategoryBasicSO unitCategoryBasicSO);
    
    UnitCategorySO mapUnitCategoryToUnitCategorySO(UnitCategory unitCategory);
    
    UnitCategoryBasicSO mapUnitCategoryToUnitCategoryBasicSO(UnitCategory unitCategory);
    
    List<UnitCategorySO> mapUnitCategoryListToUnitCategorySOList(List<UnitCategory> unitCategories);
    
    List<UnitCategoryBasicSO> mapUnitCategoryListToUnitCategoryBasicSOList(List<UnitCategory> unitCategories);

}
