package sk.filo.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Category;

/**
 *
 * @author tomas
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
