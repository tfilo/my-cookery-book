package sk.filo.recipes.so;

import java.text.Normalizer;
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
    
    public String getTitle() {
        if (title!=null) {
            String titleSearch = Normalizer.normalize(title, Normalizer.Form.NFD);
            titleSearch = titleSearch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            return titleSearch.toLowerCase();
        }
        return null;
    }
}
