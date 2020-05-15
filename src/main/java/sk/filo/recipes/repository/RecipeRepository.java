package sk.filo.recipes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Recipe;

/**
 *
 * @author tomas
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    
    public List<Recipe> findFirst4ByCategoriesId(Long id);
    
}
