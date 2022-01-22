package sk.filo.recipes.so.view;

import java.text.DecimalFormat;
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
    
    private Float value;
    
    private String formattedValue;
    
    private UnitViewSO unit;
    
    public String getFormattedValue() {
        if (value == null) {
            return "";
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        return decimalFormat.format(value);
    }
}
