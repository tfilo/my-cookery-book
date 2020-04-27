package sk.filo.recipes.so;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author tomas
 */
@Getter
@Setter
public class UnitCategorySO {

    Long id;
    
    @NotBlank
    @Size(max=255)
    String name;
    
    List<UnitSO> units;
}
