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
import sk.filo.recipes.service.UnitCategoryService;
import sk.filo.recipes.so.UnitCategoryBasicSO;
import sk.filo.recipes.so.UnitCategorySO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/admin/unitcategory")
public class UnitCategoryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitCategoryController.class);
    
    private static final String MODEL_CATEGORIES = "categories";
    
    private static final String MODEL_UNIT_CATEGORY_SO = "unitCategoryBasicSO";
    
    @Autowired
    UnitCategoryService unitCategoryService;
    
    private void getAllCategories(final Model model) {
        List<UnitCategorySO> categories = unitCategoryService.getAll();
        model.addAttribute(MODEL_CATEGORIES, categories);
    }
    
    @RequestMapping(value="/all")
    public String getCategories(final Model model) {
        LOGGER.debug("Get all unitCategories with units");
        getAllCategories(model);
        return "unitCategories";
    }
    
    @RequestMapping(value="/save")
    public String saveUnitCategory(final Model model, @Valid UnitCategoryBasicSO unitCategoryBasicSO, final BindingResult bindingResult) {
        LOGGER.debug("Save unitCategory action {}", unitCategoryBasicSO);
        if (bindingResult.hasErrors()) {
            return "fragments/unitCategory::unitCategoryForm";
        }
        unitCategoryService.save(unitCategoryBasicSO);
        getAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
    
    @RequestMapping(value="/add")
    public String addUnitCategory(final Model model) {
        LOGGER.debug("Create unitCategory action");
        model.addAttribute(MODEL_UNIT_CATEGORY_SO, new UnitCategoryBasicSO());
        return "fragments/unitCategory::unitCategoryForm";
    }
    
    @RequestMapping(value="/delete")
    public String deleteUnitCategory(final Model model, UnitCategorySO unitCategory, final BindingResult bindingResult) {
        LOGGER.debug("Delete unitCategory action {}", unitCategory);
        unitCategoryService.delete(unitCategory.getId());
        getAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
    
    @RequestMapping(value="/cancel")
    public String cancelUnitCategory(final Model model) {
        LOGGER.debug("Cancel unitCategory action");
        getAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getUnitCategoryById(@PathVariable Long id, final Model model) {
        LOGGER.debug("Get unitCategory by id {}", id);
        UnitCategoryBasicSO unitCategory = unitCategoryService.get(id);
        model.addAttribute(MODEL_UNIT_CATEGORY_SO, unitCategory);
        return "fragments/unitCategory::unitCategoryForm";
    }
}
