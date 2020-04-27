package sk.filo.recipes.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.domain.Category;
import sk.filo.recipes.domain.Role;
import sk.filo.recipes.domain.RoleName;
import sk.filo.recipes.domain.User;
import sk.filo.recipes.mapper.CategoryMapper;
import sk.filo.recipes.repository.CategoryRepository;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.UserSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class CategoryService {

    private CategoryMapper categoryMapper;
    
    private CategoryRepository categoryRepository;
    
    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }
    
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    @Transactional
    public void save(CategorySO categorySO) {
        LOGGER.debug("save categorySO {}", categorySO);
        Category category;
        if (Objects.isNull(categorySO.getId())) {
            category = categoryMapper.mapCategorySOToCategory(categorySO);
        } else {
            category = categoryRepository.getOne(categorySO.getId());
            category.setName(categorySO.getName());
        }
        
        LOGGER.debug("save category {}", category);
        categoryRepository.save(category);
    }
    
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
    
    public List<CategorySO> getAll() {
        List<Category> allUsers = categoryRepository.findAll();
        return categoryMapper.mapCategoryListToCategorySOList(allUsers);
    }
    
    public CategorySO get(Long id) {
        Category category = categoryRepository.getOne(id);
        return categoryMapper.mapCategoryToCategorySO(category);
    }
}
