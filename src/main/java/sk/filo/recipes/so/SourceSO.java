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
public class SourceSO {
    
    Long id;
    
    @NotBlank
    @Size(max=1000)
    String url;
    
}
