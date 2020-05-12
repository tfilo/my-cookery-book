package sk.filo.recipes.controller;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.service.UnitCategoryService;
import sk.filo.recipes.service.UnitService;
import sk.filo.recipes.so.UnitCategoryBasicSO;
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
    
    private static final String MODEL_UNIT_SO = "unitSO";
    
    private static final String MODEL_CATEGORIES = "categories";
    
    @Autowired
    UnitService unitService;
    
    @Autowired
    UnitCategoryService unitCategoryService;
    
    @ModelAttribute("availableUnitCategories")
    public List<UnitCategoryBasicSO> populateCategories() {
        return unitCategoryService.getAllBasic();
    }
    
    private void getAllCategories(final Model model) {
        List<UnitCategorySO> categories = unitCategoryService.getAll();
        model.addAttribute(MODEL_CATEGORIES, categories);
    }
        
    @RequestMapping(value="/save")
    public String saveUnit(final Model model, @Valid UnitSO unit, final BindingResult bindingResult) {
        LOGGER.debug("Save unit action {}", unit);
        if (bindingResult.hasErrors()) {
            return "fragments/unit::unitForm";
        }
        unitService.save(unit);
        getAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
    
    @RequestMapping(value="/add/{category_id}")
    public String addUnit(@PathVariable Long category_id, final Model model) {
        LOGGER.debug("Create unit action");
        UnitSO unitSO = new UnitSO();
        unitSO.setUnitCategoryId(category_id);
        model.addAttribute(MODEL_UNIT_SO, unitSO);
        return "fragments/unit::unitForm";
    }
    
    @RequestMapping(value="/delete")
    public String deleteUnit(final Model model, UnitSO unit, final BindingResult bindingResult) {
        LOGGER.debug("Delete unit action {}", unit);
        unitService.delete(unit.getId());
        getAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getUnitById(@PathVariable Long id, final Model model) {
        LOGGER.debug("Get unit by id {}", id);
        UnitSO unit = unitService.get(id);
        model.addAttribute(MODEL_UNIT_SO, unit);
        return "fragments/unit::unitForm";
    }
}
