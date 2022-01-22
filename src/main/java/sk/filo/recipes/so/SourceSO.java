package sk.filo.recipes.so;

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
public class SourceSO {
    
    private Long id;
    
    @NotBlank
    @Size(max=1000)
    private String url;
    
}
