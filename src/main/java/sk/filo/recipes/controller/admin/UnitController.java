package sk.filo.recipes.controller.admin;

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
import sk.filo.recipes.controller.ModelAttributeConstants;
import sk.filo.recipes.service.UnitCategoryService;
import sk.filo.recipes.service.UnitService;
import sk.filo.recipes.so.UnitCategorySO;
import sk.filo.recipes.so.UnitSO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/admin/unit")
public class UnitController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitController.class);
    
    @Autowired
    UnitService unitService;
    
    @Autowired
    UnitCategoryService unitCategoryService;
    
    private void setAvailableUnitCategories(final Model model) {
        model.addAttribute(ModelAttributeConstants.MODEL_AVAILABLE_UNIT_CATEGORIES, unitCategoryService.getAllBasic());
    }
    
    private void setAllCategories(final Model model) {
        List<UnitCategorySO> categories = unitCategoryService.getAll();
        model.addAttribute(ModelAttributeConstants.MODEL_CATEGORIES, categories);
    }
    
    @RequestMapping(value="/add/{category_id}")
    public String addUnit(final @PathVariable Long category_id, final Model model) {
        LOGGER.debug("Create unit action");
        UnitSO unitSO = new UnitSO();
        unitSO.setUnitCategoryId(category_id);
        model.addAttribute(ModelAttributeConstants.MODEL_UNIT_SO, unitSO);
        setAvailableUnitCategories(model);
        return "fragments/unit::unitForm";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getUnitById(final @PathVariable Long id, final Model model) {
        LOGGER.debug("Get unit by id {}", id);
        UnitSO unit = unitService.get(id);
        model.addAttribute(ModelAttributeConstants.MODEL_UNIT_SO, unit);
        setAvailableUnitCategories(model);
        return "fragments/unit::unitForm";
    }
    
    @RequestMapping(value="/save")
    public String saveUnit(final Model model, @Valid UnitSO unit, final BindingResult bindingResult) {
        LOGGER.debug("Save unit action {}", unit);
        if (bindingResult.hasErrors()) {
            return "fragments/unit::unitForm";
        }
        unitService.save(unit);
        setAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }

    @RequestMapping(value="/delete/{unitId}")
    public String deleteUnit(final Model model, final @PathVariable Long unitId) {
        LOGGER.debug("Delete unit action {}", unitId);
        unitService.delete(unitId);
        setAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }

}
