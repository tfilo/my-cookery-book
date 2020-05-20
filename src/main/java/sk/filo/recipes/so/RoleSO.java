package sk.filo.recipes.so;

import javax.validation.constraints.NotBlank;
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
public class RoleSO {
    
    private Long id;

    @NotBlank
    private String name;
    
}
