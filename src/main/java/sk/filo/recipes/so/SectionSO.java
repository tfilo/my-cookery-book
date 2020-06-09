package sk.filo.recipes.so;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Min;
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
public class SectionSO {
    
    private Long id;
    
    @Min(value=0)
    private Integer sortNumber;
    
    @Size(max=80)
    private String name;
    
    @Valid
    private final List<IngredientSO> ingredients = new ArrayList<>();
        
    @NotBlank
    @Size(max=Integer.MAX_VALUE)
    private String method;
}
