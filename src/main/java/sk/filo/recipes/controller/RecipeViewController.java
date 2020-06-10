package sk.filo.recipes.controller;

import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sk.filo.recipes.component.PDFGenerator;
import sk.filo.recipes.component.Search;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.PictureService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.service.TagService;
import sk.filo.recipes.so.PictureSO;
import sk.filo.recipes.so.RecipeSearchCriteriaSO;
import sk.filo.recipes.so.TagSO;
import sk.filo.recipes.so.view.RecipeViewSO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/view")
public class RecipeViewController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeViewController.class);
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    PictureService pictureService;
    
    @Autowired
    TagService tagService;
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    Search search;
    
    @Autowired
    PDFGenerator pdfGenerator;

    @RequestMapping(value="recipe/{recipeId}")
    public String viewRecipe(final Model model, final @PathVariable Long recipeId, final HttpServletRequest req) {
        LOGGER.debug("View recipe by id {}", recipeId);
        RecipeViewSO recipeSO = recipeService.getView(recipeId);
        LOGGER.debug("Loaded recipe view {}", recipeSO);
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPE_VIEW_SO, recipeSO);
        return "fragments/view::recipeView";
    }
    
    @RequestMapping(value="/categoriesPreview")
    public String viewRecipeCategoriesPreview(final Model model, final HttpServletRequest req) {
        return search.showPreview(model, req);
    }
    
    @RequestMapping(value={"/back"})
    public String backToCategory(final Model model, final HttpServletRequest req) {
        return search.backToCategory(model, req);
    }

    @RequestMapping(value={"/find"})
    public String find(final Model model, final RecipeSearchCriteriaSO searchCriteria, final HttpServletRequest req) {
        return search.searchByCriteria(model, searchCriteria, req);
    }
    
    @RequestMapping(value={"/find/addTag"})
    public String addTagAndFind(final Model model, final Long tagId, final HttpServletRequest req) {
        TagSO tag = tagService.get(tagId);
        HttpSession session = req.getSession();
        Object obj = session.getAttribute(ModelAttributeConstants.SEARCH_CRITERIA);
        RecipeSearchCriteriaSO searchCriteria;
        if (obj == null) {
            searchCriteria = new RecipeSearchCriteriaSO();
        } else {
            searchCriteria = (RecipeSearchCriteriaSO)obj;
        }
        
        if (!searchCriteria.getTags().contains(tag)) {
            searchCriteria.getTags().add(tag);
        }
        searchCriteria.setPage(0);
        return search.searchByCriteria(model, searchCriteria, req);
    }
    
    @RequestMapping(value={"/find/removeTag/{tagRowIdx}"})
    public String removeTagAndFind(final Model model, final RecipeSearchCriteriaSO searchCriteria, final @PathVariable Long tagRowIdx, final HttpServletRequest req) {        
        searchCriteria.getTags().remove(tagRowIdx.intValue());
        searchCriteria.setPage(0);
        return search.searchByCriteria(model, searchCriteria, req);
    }
        
    @RequestMapping(value = "/picture/thumbnail/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPictureThumbnailById(final @PathVariable Long id) {
        LOGGER.debug("Getting picture thumbnail by id");
        PictureSO pictureSO= pictureService.getThumbnail(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
            
        return new ResponseEntity<>(pictureSO.getData(), headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/picture/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPictureById(final @PathVariable Long id) {
        LOGGER.debug("Getting picture by id");
        PictureSO pictureSO= pictureService.get(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
            
        return new ResponseEntity<>(pictureSO.getData(), headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/picture/thumbnail/byrecipe/{recipeId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPictureThumbnailByRecipeId(final @PathVariable Long recipeId) {
        LOGGER.debug("Getting picture thumbnail by recipeId");
        PictureSO pictureSO= pictureService.getThumbnailByRecipeId(recipeId);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
            
        return new ResponseEntity<>(pictureSO.getData(), headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/pdf/{recipeId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generatePDFbyId(final @PathVariable Long recipeId) {
        LOGGER.debug("Getting pdf recipe id");
        RecipeViewSO recipeSO = recipeService.getView(recipeId);

        byte[] generate = pdfGenerator.generate(recipeSO);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        
        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
          .filename(recipeSO.getTitle() + ".pdf", StandardCharsets.UTF_8)
          .build();
        
        headers.setContentDisposition(contentDisposition);
        return new ResponseEntity<>(generate, headers, HttpStatus.CREATED);
    }
    
}
