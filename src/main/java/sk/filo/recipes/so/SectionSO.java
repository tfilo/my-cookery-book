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
    
    @NotBlank
    @Size(max=100)
    private String name;
    
    @Valid
    private List<IngredientSO> ingredients;
    
    @NotBlank
    @Size(max=2000)
    private String method;
        
    private List<PictureSO> pictures;
    
    public List<IngredientSO> getIngredients() {
        if (Objects.isNull(ingredients)) {
            ingredients = new ArrayList<>();
        }
        return ingredients;
    }

    public List<PictureSO> getPictures() {
        if (Objects.isNull(pictures)) {
            pictures = new ArrayList<>();
        }
        return pictures;
    }
}
