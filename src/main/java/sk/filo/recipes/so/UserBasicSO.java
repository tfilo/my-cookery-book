package sk.filo.recipes.so;

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
@ToString(exclude = {"password", "passwordConfirm"})
public class UserBasicSO {

    private String username;
    
    @Size(max=50)
    private String firstName;

    @Size(max=50)
    private String lastName;
    
    @Size(max=255)
    private String password;
    
    @Size(max=255)
    private String passwordConfirm;

}
