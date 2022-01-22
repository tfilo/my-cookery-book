package sk.filo.recipes.so;
        
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString
public class RecipeBasicSO {
        
    private Long id;
    
    @NotBlank
    @Size(max=80)
    private String title;
    
    @Size(max=255)
    private String description;
    
    private String creator;
}
