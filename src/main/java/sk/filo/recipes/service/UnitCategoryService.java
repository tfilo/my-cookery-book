package sk.filo.recipes.service;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitCategoryService.class);

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

    public void save(UnitCategoryBasicSO unitCategoryBasicSO) {
        LOGGER.debug("save unitCategoryBasicSO {}", unitCategoryBasicSO);
        UnitCategory unitCategory;
        if (Objects.isNull(unitCategoryBasicSO.getId())) {
            unitCategory = unitCategoryMapper.mapUnitCategoryBasicSOToUnitCategory(unitCategoryBasicSO);
        } else {
            unitCategory = unitCategoryRepository.findById(unitCategoryBasicSO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UnitCategory by id not found!"));
            unitCategory.setName(unitCategoryBasicSO.getName());
        }
        
        LOGGER.debug("save unitCategory {}", unitCategory);
        unitCategoryRepository.save(unitCategory);
    }
    
    public void delete(Long id) {
        try {
            unitCategoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getLocalizedMessage());
        }
    }
    
    public List<UnitCategorySO> getAll() {
        Sort sort = Sort.by(Sort.Order.asc("name"));
        List<UnitCategory> allCategories = unitCategoryRepository.findAll(sort);
        return unitCategoryMapper.mapUnitCategoryListToUnitCategorySOList(allCategories);
    }
    
    public List<UnitCategoryBasicSO> getAllBasic() {
        Sort sort = Sort.by(Sort.Order.asc("name"));
        List<UnitCategory> allCategories = unitCategoryRepository.findAll(sort);
        return unitCategoryMapper.mapUnitCategoryListToUnitCategoryBasicSOList(allCategories);
    }
    
    public UnitCategoryBasicSO get(Long id) {
        UnitCategory unit = unitCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UnitCategory not found!"));
        return unitCategoryMapper.mapUnitCategoryToUnitCategoryBasicSO(unit);
    }
    
}
