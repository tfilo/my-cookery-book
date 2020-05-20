package sk.filo.recipes.service;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.filo.recipes.domain.Picture;
import sk.filo.recipes.mapper.PictureMapper;
import sk.filo.recipes.mapper.SimplePictureMapper;
import sk.filo.recipes.repository.PictureRepository;
import sk.filo.recipes.so.PictureBasicSO;
import sk.filo.recipes.so.PictureSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class PictureService {

    private PictureMapper pictureMapper;
    
    private SimplePictureMapper simplePictureMapper;;
    
    private PictureRepository pictureRepository;
    
    @Autowired
    public void setPictureMapper(PictureMapper pictureMapper) {
        this.pictureMapper = pictureMapper;
    }
    
    @Autowired
    public void setSimplePictureMapper(SimplePictureMapper simplePictureMapper) {
        this.simplePictureMapper = simplePictureMapper;
    }
    
    @Autowired
    public void setPictureRepository(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureService.class);
    
    @Transactional
    public PictureSO get(Long id) {
        LOGGER.debug("get picture by id {}", id);
        return pictureMapper.mapPictureToPictureSO(pictureRepository.getOne(id));
    }
    
    @Transactional
    public PictureSO getThumbnailByRecipeId(Long recipeId) {
        LOGGER.debug("get picture thumbnail by recipeId {}", recipeId);   
        return pictureMapper.mapPictureToPictureSOThumbnail(pictureRepository.findTopByRecipeId(recipeId));
    }
    
    @Transactional
    public PictureBasicSO save(PictureSO so) {
        try {
            LOGGER.debug("save picture {}", so.toString());
            Picture picture = pictureMapper.mapPictureSOToPicture(so);
            picture.setUploaded(LocalDateTime.now());
            picture.setThumbnail(generateThumbnail(picture.getData()));
            pictureRepository.deleteOrphanedPictures(LocalDateTime.now().minusHours(2));
            
            return simplePictureMapper.mapPictureToPictureBasicSO(pictureRepository.saveAndFlush(picture));
        } catch (IOException ex) {
            LOGGER.debug(ex.getMessage());
            throw new RuntimeException("Generating thumbnail failed.");
        }
    }
    
    private byte[] generateThumbnail(byte[] picture) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(picture);
        BufferedImage bImage = ImageIO.read(bis);
        ImageIO.write(bImage, "jpg", new File("thumbnail.jpg"));
        
        BufferedImage asBufferedImage = Thumbnails.of(bImage).size(350, 350).asBufferedImage();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(asBufferedImage, "jpg", bos);
        return bos.toByteArray();
    }
}
