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
    
    public Page<Recipe> findAllByCategoriesId(Long id, Pageable page);
    
    public Page<Recipe> findAllByTitleSearchIsContaining(String titleSearch, Pageable page);
    
    public Page<Recipe> findAllByCategoriesIdAndTitleSearchIsContaining(Long categoryId, String titleSearch, Pageable page);
    
    public List<Recipe> findTop4ByTitleSearchIsContainingAndIdNot(String titleSearch, Long id, Sort sort);

    public List<Recipe> findTop4ByTitleSearchIsContaining(String titleSearch, Sort sort);
    
    public Long countByAssociatedRecipesId(Long id);

    public Long countByCategoriesId(Long id);
    
    public Long countByCreatorIdOrModifierId(Long creatorId, Long modifierId);
}
