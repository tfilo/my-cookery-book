package sk.filo.recipes.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Recipe;

/**
 *
 * @author tomas
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    public List<Recipe> findTop4ByCategoriesId(Long id, Sort sort);
    
    public Page<Recipe> findAllByCategoriesId(Pageable page, Long id);
    
    public List<Recipe> findTop4ByTitleIsContainingIgnoreCase(String title, Sort sort);

}
