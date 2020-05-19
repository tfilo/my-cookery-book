package sk.filo.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Picture;

/**
 *
 * @author tomas
 */
public interface PictureRepository extends JpaRepository<Picture, Long> {

}
