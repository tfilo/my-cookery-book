package sk.filo.recipes.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString(exclude = "password")
@Entity
@Table(name = "cb_user")
@SequenceGenerator(name = "user_generator", allocationSize = 1, sequenceName = "cb_user_seq")
public class User {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "user_generator")
    private Long id;
    
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "first_name", nullable = true)
    private String firstName;
    
    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(name = "cb_user_role",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    private Set<Role> roles;
    
    public Set<Role> getRoles() {
        if (Objects.isNull(roles)) {
            roles = new HashSet<>();
        }
        return roles;
    }
}
