package sk.filo.recipes.service;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.filo.recipes.domain.Unit;
import sk.filo.recipes.domain.UnitCategory;
import sk.filo.recipes.mapper.UnitMapper;
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

    private UnitMapper unitMapper;
    
    private UnitRepository unitRepository;
    
    private UnitCategoryRepository unitCategoryRepository;
    
    @Autowired
    public void setUnitMapper(UnitMapper unitMapper) {
        this.unitMapper = unitMapper;
    }
    
    @Autowired
    public void setUnitRepository(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }
    
    @Autowired
    public void setUnitCategoryRepository(UnitCategoryRepository unitCategoryRepository) {
        this.unitCategoryRepository = unitCategoryRepository;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitService.class);
 
    public void save(UnitSO unitSO) {
        LOGGER.debug("save unitSO {}", unitSO);
        Unit unit;
        if (Objects.isNull(unitSO.getId())) {
            unit = unitMapper.mapUnitSOToUnit(unitSO);
        } else {
            unit = unitRepository.getOne(unitSO.getId());
            unit.setName(unitSO.getName());
            unit.setAbbreviation(unitSO.getAbbreviation());
        }
        unit.setCategory(unitCategoryRepository.getOne(unitSO.getUnitCategoryId()));
        
        LOGGER.debug("save unit {}", unit);
        unitRepository.save(unit);
    }
    
    public void delete(Long id) {
        unitRepository.deleteById(id);
    }
    
    public List<UnitSO> getAll() {
        List<Unit> allUnits = unitRepository.findAll();
        return unitMapper.mapUnitListToUnitSOList(allUnits);
    }
    
    public UnitSO get(Long id) {
        Unit unit = unitRepository.getOne(id);
        return unitMapper.mapUnitToUnitSO(unit);
    }
}
