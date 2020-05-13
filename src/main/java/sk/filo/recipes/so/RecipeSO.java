package sk.filo.recipes.so;
        
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author tomas
 */
@Getter
@Setter
public class RecipeSO {
    
    Long id;
    
    @NotBlank
    @Size(max=100)
    String title;
    
    @Size(max=255)
    String description;
    
    List<SectionSO> sections;
    
    List<RecipeSO> associatedRecipes;
    
    @NotNull
    List<CategorySO> categories;
    
    List<SourceSO> sources;
    
    String creator;
    
    String modifier;
    
    LocalDateTime created;
    
    LocalDateTime modified;
}
