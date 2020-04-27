package sk.filo.recipes.so;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author tomas
 */
@Getter
@Setter
public class UnitSO {

    private Long id;

    @NotBlank
    @Size(max=255)
    private String name;

    @NotNull
    private Long unitCategoryId;
}
