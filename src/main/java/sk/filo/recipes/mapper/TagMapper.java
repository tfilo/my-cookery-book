package sk.filo.recipes.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Tag;
import sk.filo.recipes.so.TagSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface TagMapper {

    Tag mapTagSOToTag(TagSO tagSO);

    TagSO mapTagToTagSO(Tag tag);

    List<TagSO> mapTagListToTagSOList(List<Tag> tags);

}
