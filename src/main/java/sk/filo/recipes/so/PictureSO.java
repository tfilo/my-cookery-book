package sk.filo.recipes.so;

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
public class PictureSO {
    
    Long id;
    
    @NotBlank
    @Size(max=255)
    String title;
}
