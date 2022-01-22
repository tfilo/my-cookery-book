package sk.filo.recipes.domain;

/**
 *
 * @author tomas
 */
public enum RoleName {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_EDITOR;
    
    public String getSimpleName() {
        return this.name().substring(5);
    }
}
