package sk.filo.recipes.service;

import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.domain.Unit;
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitService.class);

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
 
    public void save(UnitSO unitSO) {
        LOGGER.debug("save unitSO {}", unitSO);
        Unit unit = unitMapper.mapUnitSOToUnit(unitSO);
        unit.setCategory(unitCategoryRepository.findById(unitSO.getUnitCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found!")));
        LOGGER.debug("save unit {}", unit);
        unitRepository.save(unit);
    }
    
    public void delete(Long id) {
        try {
            unitRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getLocalizedMessage());
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
