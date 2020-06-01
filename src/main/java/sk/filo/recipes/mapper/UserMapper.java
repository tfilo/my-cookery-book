package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.User;
import sk.filo.recipes.so.UserBasicSO;
import sk.filo.recipes.so.UserSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

    @Mappings({
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "roles", ignore = true)
    })
    User mapUserSOToUser(UserSO userSO);
    
    @Mappings({
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "roles", ignore = true)
    })
    void mapUserSOToUser(UserSO userSO, @MappingTarget User user);
    
    @Mappings({
        @Mapping(target = "username", ignore = true),
        @Mapping(target = "password", ignore = true)
    })
    void mapUserBasicSOToUser(UserBasicSO userBasicSO, @MappingTarget User user);

    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "roles", ignore = true)
    })
    UserSO mapUserToUserSO(User user);
    
    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    UserBasicSO mapUserToUserBasicSO(User user);

    @AfterMapping
    static void mapUserToUserSO(User user, @MappingTarget UserSO userSO) {
        user.getRoles().forEach((role) -> {
            userSO.getRoles().add(role.getName().name());
        });
    }

    List<UserSO> mapUserListToUserSOList(List<User> users);

}
