package sk.filo.recipes.so;

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
public class IngredientSO {
    
    Long id;
    
    @NotNull
    @Min(value=0)
    Integer sortNumber;
    
    @NotBlank
    @Size(max=255)
    String name;
    
    @NotNull
    Integer value;
    
    UnitSO unit;
}
