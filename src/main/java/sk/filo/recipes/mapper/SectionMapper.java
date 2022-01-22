package sk.filo.recipes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Section;
import sk.filo.recipes.so.SectionSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = { SimplePictureMapper. class }
)
public interface SectionMapper {

    @Mappings({
        @Mapping(target = "ingredients", ignore = true)
    })
    Section mapSectionSOToSection(SectionSO sectionSO);
    
    SectionSO mapSectionToSectionSO(Section section);

}
