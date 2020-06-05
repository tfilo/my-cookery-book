package sk.filo.recipes.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureService.class);

    @Autowired
    private PictureMapper pictureMapper;
    
    @Autowired
    private SimplePictureMapper simplePictureMapper;;
    
    @Autowired
    private PictureRepository pictureRepository;
    
    @Transactional
    public PictureSO get(Long id) {
        LOGGER.debug("get picture by id {}", id);
        return pictureMapper.mapPictureToPictureSO(pictureRepository.getOne(id));
    }
    
    @Transactional
    public PictureSO getThumbnail(Long id) {
        LOGGER.debug("get picture thumbnail by id {}", id);   
        return pictureMapper.mapPictureToPictureSOThumbnail(pictureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Picture thumbnail not found!")));
    }
    
    @Transactional
    public PictureSO getThumbnailByRecipeId(Long recipeId) {
        LOGGER.debug("get picture thumbnail by recipeId {}", recipeId);  
        
        Picture picture = pictureRepository.findTopByRecipeId(recipeId);
        if (picture == null || picture.getThumbnail() == null) {
            LOGGER.debug("Picture is null, reading default.");
            picture = getDefaultPictureThumbnail();
        } else {
            LOGGER.debug("Picture found.");
        }
        
        return pictureMapper.mapPictureToPictureSOThumbnail(picture);
    }
    
    private Picture getDefaultPictureThumbnail() {
        Picture p = new Picture();
        try {
            p.setThumbnail(this.getClass().getClassLoader().getResourceAsStream("static/image/default_meal.jpg").readAllBytes());
        } catch (IOException ex) {
            LOGGER.debug("Can't load picture. {}", ex);
        }
        return p;
    }
    
    @Transactional
    public PictureBasicSO save(PictureSO so) {
        try {
            LOGGER.debug("save picture {}", so.toString());
            Picture picture = pictureMapper.mapPictureSOToPicture(so);
            picture.setUploaded(LocalDateTime.now());
            picture.setThumbnail(resize(picture.getData(), 300f));
            picture.setData(resize(picture.getData(), 1920f));
            
            // delete all 2 hours old and older pictures not assigned to any recipe
            pictureRepository.deleteOrphanedPictures(LocalDateTime.now().minusHours(2));
            
            return simplePictureMapper.mapPictureToPictureBasicSO(pictureRepository.saveAndFlush(picture));
        } catch (IOException ex) {
            LOGGER.debug(ex.getMessage());
            throw new RuntimeException("Generating thumbnail failed.");
        }
    }
    
    private byte[] resize(byte[] picture, float maxSizeLength) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(picture);
        BufferedImage bImage = ImageIO.read(bis);
        
        int height = bImage.getHeight();
        int width = bImage.getWidth();
        
        Float newHeight;
        Float newWidth;
        
        if (height < width) {
            newHeight = maxSizeLength;
            newWidth = width / (height / maxSizeLength);        
        } else {
            newWidth = maxSizeLength;
            newHeight = height / (width / maxSizeLength);       
        }
        
        Image scaled = bImage.getScaledInstance(newWidth.intValue(), newHeight.intValue(), Image.SCALE_SMOOTH);
        
        BufferedImage result = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = result.createGraphics();
        g2.drawImage(scaled, null, null);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(result, "jpg", bos);
        return bos.toByteArray();
    }
}
