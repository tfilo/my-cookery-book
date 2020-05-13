package sk.filo.recipes.so;

import java.util.List;
import javax.validation.constraints.Min;
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
public class SectionSO {
    
    Long id;
    
    @NotNull
    @Min(value=0)
    Integer sortNumber;
    
    @NotBlank
    @Size(max=255)
    String name;
    
    List<IngredientSO> ingredients;
    
    @NotBlank
    @Size(max=255)
    String method;
    
    List<UnitSO> units;
    
    List<PictureSO> pictures;
}
