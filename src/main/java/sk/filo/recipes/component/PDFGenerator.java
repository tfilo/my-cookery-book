package sk.filo.recipes.component;

import com.lowagie.text.pdf.BaseFont;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import org.bouncycastle.util.encoders.Base64;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import sk.filo.recipes.service.PictureService;
import sk.filo.recipes.so.PictureSO;
import sk.filo.recipes.so.view.RecipeViewSO;

/**
 *
 * @author tomas
 */
@Component
public class PDFGenerator {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFGenerator.class);
    
    private static final String MODEL_RECIPE_VIEW_SO = "recipeViewSO";
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    PictureService pictureService;
    
    @Autowired
    ISpringTemplateEngine templateEngine;

    public byte[] generate(RecipeViewSO recipeSO) {
        String html = parseThymeleafTemplate(recipeSO);
        LOGGER.debug(html);
        ByteArrayOutputStream outputStream;
        try {
            outputStream = generatePdfOutputStreamFromHtml(html);
        } catch (com.lowagie.text.DocumentException | IOException | DocumentException ex) {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("recipe.pdf.failed");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
        
        return outputStream.toByteArray();
    }
    
    private ByteArrayOutputStream generatePdfOutputStreamFromHtml(String html) throws IOException, DocumentException, com.lowagie.text.DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();

        renderer.getFontResolver().addFont("fonts/verdana.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        renderer.getFontResolver().addFont("fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
        return outputStream;
    }
    
    private String parseThymeleafTemplate(RecipeViewSO recipeSO) {        
        Context context = new Context();                
        context.setVariable(MODEL_RECIPE_VIEW_SO, recipeSO);
        context.setLocale(new Locale("sk"));

        for (int i = 0; i < recipeSO.getPictures().size(); i++) {
            PictureSO thumbail = pictureService.getThumbnail(recipeSO.getPictures().get(i).getId());
            recipeSO.getPictures().get(i).setBase64(Base64.toBase64String(thumbail.getData()));
        }
        
        for (int j = 0; j < recipeSO.getAssociatedRecipes().size(); j++) {
            RecipeViewSO recipeSO2 = recipeSO.getAssociatedRecipes().get(j);
            for (int i = 0; i < recipeSO2.getPictures().size(); i++) {
                PictureSO thumbail = pictureService.getThumbnail(recipeSO2.getPictures().get(i).getId());
                recipeSO2.getPictures().get(i).setBase64(Base64.toBase64String(thumbail.getData()));
            }
        }

        return templateEngine.process("pdf/recipe", context);
    }
}
