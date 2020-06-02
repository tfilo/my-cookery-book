package sk.filo.recipes.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle.util.encoders.Base64;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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
    TemplateEngine templateEngine;
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    PictureService pictureService;
    
    public byte[] generate(RecipeViewSO recipeSO) {
        String html = parseThymeleafTemplate(recipeSO);
        
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
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
        return outputStream;
    }
    
    private String parseThymeleafTemplate(RecipeViewSO recipeSO) {
        Context context = new Context();                
        context.setVariable(MODEL_RECIPE_VIEW_SO, recipeSO);
        
        for (int i = 0; i < recipeSO.getPictures().size(); i++) {
            PictureSO thumbail = pictureService.getThumbnail(recipeSO.getPictures().get(i).getId());
            recipeSO.getPictures().get(i).setBase64(Base64.toBase64String(thumbail.getData()));
            LOGGER.debug("Obrazok: " + recipeSO.getPictures().get(i).getBase64());
        }
        
        return templateEngine.process("pdf/pdf_template", context);
    }
}
