package sk.filo.recipes.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sk.filo.recipes.domain.Recipe;

/**
 *
 * @author tomas
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    
    public List<Recipe> findTop4ByCategoryId(Long id, Sort sort);
  
    public List<Recipe> findTop4ByTitleSearchIsContainingAndIdNot(String titleSearch, Long id, Sort sort);

    public List<Recipe> findTop4ByTitleSearchIsContaining(String titleSearch, Sort sort);
    
    public Long countByAssociatedRecipesId(Long id);

    public Long countByCategoryId(Long id);
    
    public Long countByTagsId(Long id);
    
    public Long countByCreatorIdOrModifierId(Long creatorId, Long modifierId);
}
