package sk.filo.recipes.service;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.filo.recipes.domain.UnitCategory;
import sk.filo.recipes.mapper.UnitCategoryMapper;
import sk.filo.recipes.repository.UnitCategoryRepository;
import sk.filo.recipes.so.UnitCategoryBasicSO;
import sk.filo.recipes.so.UnitCategorySO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class UnitCategoryService {

    private UnitCategoryMapper unitCategoryMapper;
    
    private UnitCategoryRepository unitCategoryRepository;
    
    @Autowired
    public void setUnitCategoryMapper(UnitCategoryMapper unitCategoryMapper) {
        this.unitCategoryMapper = unitCategoryMapper;
    }
        
    @Autowired
    public void setUnitCategoryRepository(UnitCategoryRepository unitCategoryRepository) {
        this.unitCategoryRepository = unitCategoryRepository;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitCategoryService.class);

    public void save(UnitCategoryBasicSO unitCategoryBasicSO) {
        LOGGER.debug("save unitCategoryBasicSO {}", unitCategoryBasicSO);
        UnitCategory unitCategory;
        if (Objects.isNull(unitCategoryBasicSO.getId())) {
            unitCategory = unitCategoryMapper.mapUnitCategoryBasicSOToUnitCategory(unitCategoryBasicSO);
        } else {
            unitCategory = unitCategoryRepository.getOne(unitCategoryBasicSO.getId());
            unitCategory.setName(unitCategoryBasicSO.getName());
        }
        
        LOGGER.debug("save unitCategory {}", unitCategory);
        unitCategoryRepository.save(unitCategory);
    }
    
    public void delete(Long id) {
        unitCategoryRepository.deleteById(id);
    }
    
    public List<UnitCategorySO> getAll() {
        List<UnitCategory> allCategories = unitCategoryRepository.findAll();
        return unitCategoryMapper.mapUnitCategoryListToUnitCategorySOList(allCategories);
    }
    
    public List<UnitCategoryBasicSO> getAllBasic() {
        List<UnitCategory> allCategories = unitCategoryRepository.findAll();
        return unitCategoryMapper.mapUnitCategoryListToUnitCategoryBasicSOList(allCategories);
    }
    
    public UnitCategoryBasicSO get(Long id) {
        UnitCategory unit = unitCategoryRepository.getOne(id);
        return unitCategoryMapper.mapUnitCategoryToUnitCategoryBasicSO(unit);
    }
    
}
