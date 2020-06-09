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
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.so.CategorySO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/admin/category")
public class CategoryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    
    @Autowired
    CategoryService categoryService;
        
    private void setAllCategories(final Model model) {
        model.addAttribute(ModelAttributeConstants.MODEL_CATEGORIES, categoryService.getAll());
    }
    
    @RequestMapping(value="/all")
    public String getCategories(final Model model) {
        LOGGER.debug("Get all categories");
        setAllCategories(model);
        return "fragments/category :: categoriesList";
    }
    
    @RequestMapping(value="/add")
    public String addCategory(final Model model) {
        LOGGER.debug("Create category action");
        model.addAttribute(ModelAttributeConstants.MODEL_CATEGORY_SO, new CategorySO());
        return "fragments/category::categoryForm";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getCategoryById(final @PathVariable Long id, final Model model) {
        LOGGER.debug("Get category by id {}", id);
        CategorySO category = categoryService.get(id);
        model.addAttribute(ModelAttributeConstants.MODEL_CATEGORY_SO, category);
        return "fragments/category::categoryForm";
    }
    
    @RequestMapping(value="/save")
    public String saveCategory(final Model model, final @Valid CategorySO category, final BindingResult bindingResult) {
        LOGGER.debug("Save category action {}", category);
        if (bindingResult.hasErrors()) {
            return "fragments/category::categoryForm";
        }
        categoryService.save(category);
        setAllCategories(model);
        model.addAttribute(ModelAttributeConstants.RELOAD_MENU, true);
        return "fragments/category::categoriesList";
    }
    
    @RequestMapping(value="/delete/{categoryId}")
    public String deleteCategory(final Model model, final @PathVariable Long categoryId) {
        LOGGER.debug("Delete category action {}", categoryId);
        categoryService.delete(categoryId);
        setAllCategories(model);
        model.addAttribute(ModelAttributeConstants.RELOAD_MENU, true);
        return "fragments/category::categoriesList";
    }
    
    @RequestMapping(value = "/reloadMenu")
    public String reloadMenu(final Model model) {
        LOGGER.debug("Reloading menu");
        model.addAttribute(ModelAttributeConstants.MODEL_CATEGORIES, categoryService.getAll());
        return "home::menu";
    }
}
