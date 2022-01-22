package sk.filo.recipes.so;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class IngredientSO {
    
    private Long id;
    
    @Min(value=0)
    private Integer sortNumber;
    
    @NotBlank
    @Size(max=80)
    private String name;
    
    private Float value;
    
    @NotNull
    private Long unitId;
}
