package sk.filo.recipes.so;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
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
@ToString(exclude = {"password", "passwordConfirm"})
public class UserSO {

    private Long id;

    @NotBlank
    @Size(max=255, min=4)
    private String username;

    @Size(max=255)
    private String firstName;

    @Size(max=255)
    private String lastName;
    
    @Size(max=255)
    private String password;
    
    @Size(max=255)
    private String passwordConfirm;
    
    @NotNull
    private Boolean enabled;
    
    private Set<String> roles;
    
    public Set<String> getRoles() {
        if (Objects.isNull(roles)) {
            roles = new LinkedHashSet<>();
        }
        return roles;
    }
}
