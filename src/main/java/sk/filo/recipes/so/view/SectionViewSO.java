package sk.filo.recipes.so.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class SectionViewSO {
    
    private String name;
    
    private List<IngredientViewSO> ingredients;
    
    private String method;
        
    private List<PictureViewSO> pictures;
    
    public List<IngredientViewSO> getIngredients() {
        if (Objects.isNull(ingredients)) {
            ingredients = new ArrayList<>();
        }
        return ingredients;
    }

    public List<PictureViewSO> getPictures() {
        if (Objects.isNull(pictures)) {
            pictures = new ArrayList<>();
        }
        return pictures;
    }
}
