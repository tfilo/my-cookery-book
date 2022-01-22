package sk.filo.recipes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.recipes.domain.User;

/**
 *
 * @author tomas
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    public Optional<User> findByUsername(String username);
    
}
