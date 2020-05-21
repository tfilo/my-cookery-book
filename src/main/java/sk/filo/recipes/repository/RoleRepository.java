package sk.filo.recipes.repository;

import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.Role;
import sk.filo.recipes.domain.RoleName;

/**
 *
 * @author tomas
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
 
    public Optional<Role> findByName(RoleName roleName, Sort sort);
    
}
