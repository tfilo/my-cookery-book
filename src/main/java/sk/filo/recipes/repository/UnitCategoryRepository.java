package sk.filo.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.UnitCategory;

/**
 *
 * @author tomas
 */
public interface UnitCategoryRepository extends JpaRepository<UnitCategory, Long> {
    
}
