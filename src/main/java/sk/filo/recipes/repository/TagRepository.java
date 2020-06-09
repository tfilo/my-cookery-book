package sk.filo.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Tag;

/**
 *
 * @author tomas
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Tag findByName(String name);

}
