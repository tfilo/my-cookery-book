package sk.filo.recipes.service;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.filo.recipes.domain.Category;
import sk.filo.recipes.domain.Recipe;
import sk.filo.recipes.mapper.CategoryMapper;
import sk.filo.recipes.mapper.RecipeMapper;
import sk.filo.recipes.repository.CategoryRepository;
import sk.filo.recipes.repository.RecipeRepository;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.CategoryWithRecipeBasicSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class CategoryService {

    private CategoryMapper categoryMapper;
    
    private RecipeMapper recipeMapper;
    
    private CategoryRepository categoryRepository;
    
    private RecipeRepository recipeRepository;
    
    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }
    
    @Autowired
    public void setRecipeMapper(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }
    
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Autowired
    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    @Transactional
    public void save(CategorySO categorySO) {
        LOGGER.debug("save categorySO {}", categorySO);
        categoryRepository.save(categoryMapper.mapCategorySOToCategory(categorySO));
    }
    
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
    
    public List<CategorySO> getAll() {
        List<Category> allUsers = categoryRepository.findAll();
        return categoryMapper.mapCategoryListToCategorySOList(allUsers);
    }
    
    public List<CategoryWithRecipeBasicSO> getFist4RecipesForEveryCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryWithRecipeBasicSO> list = categoryMapper.mapCategoryListToCategoryWithRecipeBasicSOList(categories);
        
        list.forEach((so) -> {
            List<Recipe> recipes = recipeRepository.findTop4ByCategoriesIdOrderByCreatedDesc(so.getId());
            recipes.forEach((r) -> {
                so.getRecipes().add(recipeMapper.mapRecipeToRecipeBasicSO(r));
            });
        });
        
        return list;
    }
    
    public CategorySO get(Long id) {
        Category category = categoryRepository.getOne(id);
        return categoryMapper.mapCategoryToCategorySO(category);
    }
}
