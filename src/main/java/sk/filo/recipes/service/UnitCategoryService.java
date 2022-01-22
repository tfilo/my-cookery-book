package sk.filo.recipes.service;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.domain.UnitCategory;
import sk.filo.recipes.mapper.UnitCategoryMapper;
import sk.filo.recipes.repository.UnitCategoryRepository;
import sk.filo.recipes.repository.UnitRepository;
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

    @Autowired
    private UnitCategoryMapper unitCategoryMapper;
    
    @Autowired
    private UnitCategoryRepository unitCategoryRepository;
    
    @Autowired
    private UnitRepository unitRepository;
    
    @Autowired
    MessageSource messageSource;

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
        if (unitRepository.countByCategoryId(id) == 0) {
            unitCategoryRepository.deleteById(id);
        } else {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("unit.category.delete.constraint");
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
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
