package sk.filo.recipes.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString
public class RecipeSearchCriteriaSO {
    
    private Long categoryId;
    
    private String title;
    
    private Pageable page;
}
