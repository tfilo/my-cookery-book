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
public class PictureViewSO {
    
    private Long id;
    
    private String title;
    
    private String base64;
}
