package sk.filo.recipes.controller.admin;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
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
    
    private static final String MODEL_CATEGORIES = "categories";
    
    private static final String MODEL_UNIT_CATEGORY_SO = "unitCategoryBasicSO";
    
    @Autowired
    UnitCategoryService unitCategoryService;
    
    @Autowired
    MessageSource messageSource;
    
    private void setAllCategories(final Model model) {
        model.addAttribute(MODEL_CATEGORIES, unitCategoryService.getAll());
    }
    
    @RequestMapping(value="/all")
    public String getCategories(final Model model) {
        setAllCategories(model);
        return "fragments/unitCategory :: unitCategoriesList";
    }
    
    @RequestMapping(value="/add")
    public String addUnitCategory(final Model model) {
        LOGGER.debug("Create unitCategory action");
        model.addAttribute(MODEL_UNIT_CATEGORY_SO, new UnitCategoryBasicSO());
        return "fragments/unitCategory::unitCategoryForm";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getUnitCategoryById(final @PathVariable Long id, final Model model) {
        LOGGER.debug("Get unitCategory by id {}", id);
        UnitCategoryBasicSO unitCategory = unitCategoryService.get(id);
        model.addAttribute(MODEL_UNIT_CATEGORY_SO, unitCategory);
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
        try {
            unitCategoryService.delete(unitCategoryId);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("unit.category.delete.constraint");
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
        setAllCategories(model);
        return "fragments/unitCategory::unitCategoriesList";
    }
}
