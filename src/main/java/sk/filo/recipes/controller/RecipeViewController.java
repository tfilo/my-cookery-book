package sk.filo.recipes.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
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
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.component.PDFGenerator;
import sk.filo.recipes.component.Preview;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.PictureService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.so.PictureSO;
import sk.filo.recipes.so.RecipeBasicSO;
import sk.filo.recipes.so.RecipeSearchCriteriaSO;
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
    MessageSource messageSource;
    
    @Autowired
    Preview preview;
    
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
        LOGGER.debug("Getting categoriesPreview");
        
        HttpSession session = req.getSession();
        session.removeAttribute(ModelAttributeConstants.SEARCH_CRITERIA);
        
        preview.setAllCategoriesWithRecipes(model);
        return "fragments/view::recipesList";
    }
    
    @RequestMapping(value={"/back"})
    public String backToCategory(final Model model, final HttpServletRequest req) {
        HttpSession session = req.getSession();
        Object obj = session.getAttribute(ModelAttributeConstants.SEARCH_CRITERIA);
        if (obj == null) {
            return viewRecipeCategoriesPreview(model, req);
        } else {
            return viewRecipesInCategory(model, (RecipeSearchCriteriaSO)obj, req);
        }
    }
    
    @RequestMapping(value={"/find"})
    public String viewRecipesInCategory(final Model model, final RecipeSearchCriteriaSO criteria, final HttpServletRequest req) {
        LOGGER.debug("Getting recipes by criteria");

        HttpSession session = req.getSession();
        session.setAttribute(ModelAttributeConstants.SEARCH_CRITERIA, criteria);
        
        Page<RecipeBasicSO> results = recipeService.getAllBasicByCriteria(criteria);
        
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPES, results);
        String titleString = "";
        
        if (criteria.getCategoryId() != null) {
            titleString += categoryService.get(criteria.getCategoryId()).getName();
            model.addAttribute(ModelAttributeConstants.CATEGORY_ID, criteria.getCategoryId());
        }
        
        if (criteria.getTitle() != null && !StringUtils.isEmptyOrWhitespace(criteria.getTitle())) {
            if (criteria.getCategoryId() != null) {
                titleString += ": ";
            }
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("recipe.filtered.by");
            titleString += message + " '" + criteria.getTitle() + "'";
            model.addAttribute(ModelAttributeConstants.SEARCHED_TITLE, criteria.getTitle());
        }
        
        int totalPages = results.getTotalPages();
        int currentPage = results.getNumber();
        if (totalPages > 0 && totalPages < 6) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute(ModelAttributeConstants.PAGE_NUMBERS, pageNumbers);
        } else if (totalPages > 0) {
            List<Integer> pageNumbers;
            if ((currentPage - 2) > 1 && (currentPage + 2) < totalPages) {
                pageNumbers = IntStream.rangeClosed(currentPage - 2, currentPage + 2)
                .boxed()
                .collect(Collectors.toList());
                pageNumbers.add(0, 1);
                pageNumbers.add(totalPages);
            } else if ((currentPage - 2) <= 1) {
                pageNumbers = IntStream.rangeClosed(1,6)
                .boxed()
                .collect(Collectors.toList());
                pageNumbers.add(totalPages);
            } else {
                pageNumbers = IntStream.rangeClosed(totalPages - 5,totalPages)
                .boxed()
                .collect(Collectors.toList());
                pageNumbers.add(0, 1);
            }
            model.addAttribute(ModelAttributeConstants.PAGE_NUMBERS, pageNumbers);
        }

        model.addAttribute(ModelAttributeConstants.TITLE, titleString);
        return "fragments/view::recipesList";
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
