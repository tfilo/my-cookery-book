package sk.filo.recipes.so;

import java.util.ArrayList;
import java.util.List;
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
public class CategoryWithRecipeBasicSO {

    private Long id;
    
    @NotBlank
    @Size(max=80)
    private String name;
    
    private final List<RecipeBasicSO> recipes = new ArrayList<>();
}
