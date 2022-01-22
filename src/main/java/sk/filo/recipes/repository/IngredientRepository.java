package sk.filo.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Ingredient;

/**
 *
 * @author tomas
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    Long countByUnitId(Long id);

}
