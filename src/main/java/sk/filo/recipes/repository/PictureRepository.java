package sk.filo.recipes.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.filo.recipes.domain.Picture;

/**
 *
 * @author tomas
 */
public interface PictureRepository extends JpaRepository<Picture, Long> {

    @Modifying
    @Query("DELETE FROM Picture p where p.recipe IS NULL AND p.uploaded<=:uploaded")
    void deleteOrphanedPictures(@Param("uploaded") LocalDateTime uploaded);
    
    Picture findTopByRecipeId(Long recipeId);
    
}
