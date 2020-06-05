package sk.filo.recipes.controller.admin;

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
import sk.filo.recipes.so.UnitCategoryBasicSO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/admin/unitcategory")
public class UnitCategoryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitCategoryController.class);

    @Autowired
    UnitCategoryService unitCategoryService;
    
    private void setAllCategories(final Model model) {
        model.addAttribute(ModelAttributeConstants.MODEL_CATEGORIES, unitCategoryService.getAll());
    }
    
    @RequestMapping(value="/all")
    public String getCategories(final Model model) {
        setAllCategories(model);
        return "fragments/unitCategory :: unitCategoriesList";
    }
    
    @RequestMapping(value="/add")
    public String addUnitCategory(final Model model) {
        LOGGER.debug("Create unitCategory action");
        model.addAttribute(ModelAttributeConstants.MODEL_UNIT_CATEGORY_SO, new UnitCategoryBasicSO());
        return "fragments/unitCategory::unitCategoryForm";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getUnitCategoryById(final @PathVariable Long id, final Model model) {
        LOGGER.debug("Get unitCategory by id {}", id);
        UnitCategoryBasicSO unitCategory = unitCategoryService.get(id);
        model.addAttribute(ModelAttributeConstants.MODEL_UNIT_CATEGORY_SO, unitCategory);
        return "fragments/unitCategory::unitCategoryForm";
    }
    
    @RequestMapping(value="/save")
    public String saveUnitCategory(final Model model, final @Valid UnitCategoryBasicSO unitCategoryBasicSO, final BindingResult bindingResult) {
        LOGGER.debug("Save unitCategory action {}", unitCategoryBasicSO);
        if (bindingResult.hasErrors()) {
            return "fragments/unitCategory::unitCategoryForm";
        }
        unitCategoryService.save(unitCategoryBasicSO);
        setAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
    
    @RequestMapping(value="/delete/{unitCategoryId}")
    public String deleteUnitCategory(final Model model, final @PathVariable Long unitCategoryId) {
        LOGGER.debug("Delete unitCategory action {}", unitCategoryId);
        unitCategoryService.delete(unitCategoryId);
        setAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
}
