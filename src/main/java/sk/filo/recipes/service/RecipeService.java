package sk.filo.recipes.service;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.filo.recipes.domain.Recipe;
import sk.filo.recipes.domain.User;
import sk.filo.recipes.mapper.RecipeMapper;
import sk.filo.recipes.repository.RecipeRepository;
import sk.filo.recipes.repository.UserRepository;
import sk.filo.recipes.so.RecipeSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class RecipeService {

    private RecipeMapper recipeMapper;
    
    private RecipeRepository recipeRepository;
    
    private UserRepository userRepository;
    
    @Autowired
    public void setRecipeMapper(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }
    
    @Autowired
    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

    @Transactional
    public void save(RecipeSO recipeSO) {
        LOGGER.debug("save recipeSO {}", recipeSO);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.debug("auth {}", auth);
        String username = auth.getName();
        LOGGER.debug("username {}", username);
        
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
        
        LOGGER.debug("save recipe {}", recipe);
        recipeRepository.save(recipe);
    }
    
    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }
    
    public RecipeSO get(Long id) {
        Recipe recipe = recipeRepository.getOne(id);
        return recipeMapper.mapRecipeToRecipeSO(recipe);
    }
}
