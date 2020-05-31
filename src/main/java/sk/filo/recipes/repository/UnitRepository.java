package sk.filo.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Unit;

/**
 *
 * @author tomas
 */
public interface UnitRepository extends JpaRepository<Unit, Long> {
    
    Long countByCategoryId(Long id);

}
