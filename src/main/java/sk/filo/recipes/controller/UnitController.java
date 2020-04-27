package sk.filo.recipes.controller;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.service.UnitService;
import sk.filo.recipes.so.UnitSO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/admin/unit")
public class UnitController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitController.class);
    
    private static final String MODEL_UNITS = "units";
    
    private static final String MODEL_UNIT_SO = "unitSO";
    
    @Autowired
    UnitService unitService;
    
    private void getAllUnits(final Model model) {
        List<UnitSO> units = unitService.getAll();
        model.addAttribute(MODEL_UNITS, units);
    }
    
    @RequestMapping(value="/all")
    public String getUnits(final Model model) {
        LOGGER.debug("Get all units");
        getAllUnits(model);
        return "unitEditor";
    }
    
    @RequestMapping(value="/save")
    public String saveUnit(final Model model, @Valid UnitSO unit, final BindingResult bindingResult) {
        LOGGER.debug("Save unit action {}", unit);
        if (bindingResult.hasErrors()) {
            return "fragments/unit::unitForm";
        }
        unitService.save(unit);
        getUnits(model);
        return "fragments/unit::unitsList";
    }
    
    @RequestMapping(value="/add")
    public String addUnit(final Model model) {
        LOGGER.debug("Create unit action");
        model.addAttribute(MODEL_UNIT_SO, new UnitSO());
        return "fragments/unit::unitForm";
    }
    
    @RequestMapping(value="/delete")
    public String deleteUnit(final Model model, UnitSO unit, final BindingResult bindingResult) {
        LOGGER.debug("Delete unit action {}", unit);
        unitService.delete(unit.getId());
        getAllUnits(model);
        return "fragments/unit::unitsList";
    }
    
    @RequestMapping(value="/cancel")
    public String cancelUnit(final Model model, UnitSO unit, final BindingResult bindingResult) {
        LOGGER.debug("Cancel unit action {}", unit);
        getAllUnits(model);
        return "fragments/unit::unitsList";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getUnitById(@PathVariable Long id, final Model model) {
        LOGGER.debug("Get unit by id {}", id);
        UnitSO unit = unitService.get(id);
        model.addAttribute(MODEL_UNIT_SO, unit);
        return "fragments/unit::unitForm";
    }
}
