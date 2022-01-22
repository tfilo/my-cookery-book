package sk.filo.recipes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Picture;
import sk.filo.recipes.so.PictureSO;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PictureMapper {

    Picture mapPictureSOToPicture(PictureSO pictureSO);

    PictureSO mapPictureToPictureSO(Picture picture);
    
    @Mappings({
        @Mapping(target = "data", source = "thumbnail")
    })
    PictureSO mapPictureToPictureSOThumbnail(Picture picture);

}
