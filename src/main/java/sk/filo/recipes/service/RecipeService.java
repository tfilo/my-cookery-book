package sk.filo.recipes.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.domain.Category;
import sk.filo.recipes.domain.Ingredient;
import sk.filo.recipes.domain.Recipe;
import sk.filo.recipes.domain.Section;
import sk.filo.recipes.domain.User;
import sk.filo.recipes.mapper.IngredientMapper;
import sk.filo.recipes.mapper.RecipeMapper;
import sk.filo.recipes.mapper.SectionMapper;
import sk.filo.recipes.repository.CategoryRepository;
import sk.filo.recipes.repository.RecipeRepository;
import sk.filo.recipes.repository.UnitRepository;
import sk.filo.recipes.repository.UserRepository;
import sk.filo.recipes.so.IngredientSO;
import sk.filo.recipes.so.RecipeBasicSO;
import sk.filo.recipes.so.RecipeSO;
import sk.filo.recipes.so.SectionSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class RecipeService {

    private RecipeMapper recipeMapper;
    
    private SectionMapper sectionMapper;
    
    private IngredientMapper ingredientMapper;
    
    private RecipeRepository recipeRepository;
    
    private UserRepository userRepository;
    
    private CategoryRepository categoryRepository;
    
    private UnitRepository unitRepository;
    
    @Autowired
    public void setRecipeMapper(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }
    
    @Autowired
    public void setSectionMapper(SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
    }
    
    @Autowired
    public void setIngredientMapper(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }
    
    @Autowired
    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Autowired
    public void setUnitRepository(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

    @Transactional
    public void save(RecipeSO recipeSO) {
        LOGGER.debug("save recipeSO {}", recipeSO);        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authenticatedUser = userRepository.findByUsername(username);
        
        Recipe recipe;
        if (Objects.isNull(recipeSO.getId())) {
            recipe = recipeMapper.mapRecipeSOToRecipe(recipeSO);
            recipe.setCreated(LocalDateTime.now());
            recipe.setCreator(authenticatedUser);
        } else {
            recipe = recipeRepository.getOne(recipeSO.getId());
            recipeMapper.mapRecipeSOToRecipe(recipeSO, recipe);
            recipe.setModified(LocalDateTime.now());
            recipe.setModifier(authenticatedUser);
        }
        
        mapAssociatedRecipes(recipeSO.getAssociatedRecipes(), recipe.getAssociatedRecipes());
        mapCategories(recipeSO.getCategories(), recipe.getCategories());
        mapSections(recipeSO.getSections(), recipe.getSections());
        
        // TODO map pictures on recipe
        
        LOGGER.debug("save recipe {}", recipe);
        recipeRepository.save(recipe);
    }
    
    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }
    
    public RecipeSO get(Long id) {
        Recipe recipe = recipeRepository.getOne(id);
        LOGGER.debug("get recipe {}", recipe);
        return recipeMapper.mapRecipeToRecipeSO(recipe);
    }

    public List<RecipeBasicSO> getAllBasic() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipeMapper.mapRecipeListToRecipeBasicSOList(recipes);
    }
    
    private void mapAssociatedRecipes(final List<Long> associatedRecipeIds, final List<Recipe> associatedRecipes) {
        associatedRecipes.clear();
        associatedRecipeIds.forEach((Long id) -> {
            associatedRecipes.add(recipeRepository.findById(id)
                                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Associated recipe not found!")));
        });
    }

    private void mapCategories(final List<Long> categoriesIds, final List<Category> categories) {
        categories.clear();
        categoriesIds.forEach((id) -> {
            categories.add(categoryRepository.findById(id)
                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found!")));
        });
    }

    private void mapSections(final List<SectionSO> sectionSOs, final List<Section> sections) {
        sections.clear();
        sectionSOs.forEach((SectionSO so) -> {
            Section s = sectionMapper.mapSectionSOToSection(so);
            so.getIngredients().forEach((IngredientSO iso) -> {
                Ingredient i = ingredientMapper.mapIngredientSOToIngredient(iso);
                i.setUnit(unitRepository.findById(iso.getUnitId())
                          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit not found!")));
                s.getIngredients().add(i);
            });
            // TODO map pictures on section
            sections.add(s);
        });
    }
}
