package sk.filo.recipes.service;

import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.domain.Unit;
import sk.filo.recipes.mapper.UnitMapper;
import sk.filo.recipes.repository.IngredientRepository;
import sk.filo.recipes.repository.UnitCategoryRepository;
import sk.filo.recipes.repository.UnitRepository;
import sk.filo.recipes.so.UnitSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class UnitService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitService.class);

    @Autowired
    private UnitMapper unitMapper;
    
    @Autowired
    private UnitRepository unitRepository;
    
    @Autowired
    private UnitCategoryRepository unitCategoryRepository;
    
    @Autowired
    private IngredientRepository ingredientRepository;
    
    @Autowired
    MessageSource messageSource; 

    public void save(UnitSO unitSO) {
        LOGGER.debug("save unitSO {}", unitSO);
        Unit unit = unitMapper.mapUnitSOToUnit(unitSO);
        unit.setCategory(unitCategoryRepository.findById(unitSO.getUnitCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found!")));
        LOGGER.debug("save unit {}", unit);
        unitRepository.save(unit);
    }
    
    public void delete(Long id) {
        if (ingredientRepository.countByUnitId(id) == 0) {
            unitRepository.deleteById(id);
        } else {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("unit.delete.constraint");
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
    }
    
    public List<UnitSO> getAll() {
        List<Unit> allUnits = unitRepository.findAll();
        return unitMapper.mapUnitListToUnitSOList(allUnits);
    }
    
    public UnitSO get(Long id) {
        Unit unit = unitRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit not found!"));
        return unitMapper.mapUnitToUnitSO(unit);
    }
}
