package sk.filo.recipes.so;

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
public class UnitSO {

    private Long id;

    @NotBlank
    @Size(max=80)
    private String name;
    
    @NotBlank
    @Size(max=20)
    private String abbreviation;
    
    @NotNull
    private Boolean valueRequired = true;

    @NotNull
    private Long unitCategoryId;
}
