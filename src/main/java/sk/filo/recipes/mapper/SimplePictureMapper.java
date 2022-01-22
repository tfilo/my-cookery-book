package sk.filo.recipes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Picture;
import sk.filo.recipes.so.PictureBasicSO;
import sk.filo.recipes.so.PictureSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface SimplePictureMapper {

    @Mappings({
            @Mapping(target = "data", ignore = true)
    })
    Picture mapPictureSOToPicture(PictureSO pictureSO);

    PictureBasicSO mapPictureToPictureBasicSO(Picture picture);

}
