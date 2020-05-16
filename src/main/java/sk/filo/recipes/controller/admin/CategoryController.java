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
    
    private static final String MODEL_CATEGORIES = "categories";
    
    private static final String MODEL_CATEGORY_SO = "categorySO";
    
    @Autowired
    CategoryService categoryService;
        
    private void setAllCategories(final Model model) {
        model.addAttribute(MODEL_CATEGORIES, categoryService.getAll());
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
        model.addAttribute(MODEL_CATEGORY_SO, new CategorySO());
        return "fragments/category::categoryForm";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getCategoryById(@PathVariable Long id, final Model model) {
        LOGGER.debug("Get category by id {}", id);
        CategorySO category = categoryService.get(id);
        model.addAttribute(MODEL_CATEGORY_SO, category);
        return "fragments/category::categoryForm";
    }
    
    @RequestMapping(value="/save")
    public String saveCategory(final Model model, @Valid CategorySO category, final BindingResult bindingResult) {
        LOGGER.debug("Save category action {}", category);
        if (bindingResult.hasErrors()) {
            return "fragments/category::categoryForm";
        }
        categoryService.save(category);
        setAllCategories(model);
        return "fragments/category::categoriesList";
    }
    
    @RequestMapping(value="/delete")
    public String deleteCategory(final Model model, CategorySO category, final BindingResult bindingResult) {
        LOGGER.debug("Delete category action {}", category);
        categoryService.delete(category.getId());
        setAllCategories(model);
        return "fragments/category::categoriesList";
    }
}
