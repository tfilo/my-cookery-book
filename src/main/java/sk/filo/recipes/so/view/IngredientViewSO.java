package sk.filo.recipes.so.view;

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
public class IngredientViewSO {
    
    private String name;
    
    private String value;
    
    private UnitViewSO unit;
}
