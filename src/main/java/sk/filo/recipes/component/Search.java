package sk.filo.recipes.component;

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
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.controller.ModelAttributeConstants;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.so.RecipeBasicSO;
import sk.filo.recipes.so.RecipeSearchCriteriaSO;

/**
 *
 * @author tomas
 */
@Component
public class Search {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Search.class);
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    MessageSource messageSource;
    
    public String showPreview(Model model, final HttpServletRequest req) {
        LOGGER.debug("Getting categoriesPreview");
        
        RecipeSearchCriteriaSO criteria = new RecipeSearchCriteriaSO();
        criteria.setPage(0);
        criteria.setPageSize(16);
        criteria.setDirection(RecipeSearchCriteriaSO.Direction.DESC);
        criteria.setSortField(RecipeSearchCriteriaSO.SortField.created);
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        String message = accessor.getMessage("recipe.preview");
        model.addAttribute(ModelAttributeConstants.TITLE, message);
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPES, recipeService.getAllBasicByCriteria(criteria));
        model.addAttribute(ModelAttributeConstants.SEARCH_CRITERIA, criteria);
        
        HttpSession session = req.getSession();
        session.removeAttribute(ModelAttributeConstants.SEARCH_CRITERIA);
        
        return "fragments/view::recipesList";
    }
    
    public String searchByCriteria(final Model model, final RecipeSearchCriteriaSO searchCriteria, final HttpServletRequest req) {
        LOGGER.debug("Getting recipes by criteria");

        HttpSession session = req.getSession();
        session.setAttribute(ModelAttributeConstants.SEARCH_CRITERIA, searchCriteria);
        model.addAttribute(ModelAttributeConstants.SEARCH_CRITERIA, searchCriteria);
        
        Page<RecipeBasicSO> results = recipeService.getAllBasicByCriteria(searchCriteria);
        
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPES, results);
        String titleString = "";
        
        if (searchCriteria.getCategoryId() != null) {
            titleString += categoryService.get(searchCriteria.getCategoryId()).getName();
            model.addAttribute(ModelAttributeConstants.CATEGORY_ID, searchCriteria.getCategoryId());
        }
        
        if (searchCriteria.getTitle() != null && !StringUtils.isEmptyOrWhitespace(searchCriteria.getTitle())) {
            if (searchCriteria.getCategoryId() != null) {
                titleString += ": ";
            }
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("recipe.filtered.by");
            titleString += message + " '" + searchCriteria.getTitle() + "'";
            model.addAttribute(ModelAttributeConstants.SEARCHED_TITLE, searchCriteria.getTitle());
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
    
    public String backToCategory(final Model model, final HttpServletRequest req) {
        HttpSession session = req.getSession();
        Object obj = session.getAttribute(ModelAttributeConstants.SEARCH_CRITERIA);
        if (obj == null) {
            return showPreview(model, req);
        } else {
            return searchByCriteria(model, (RecipeSearchCriteriaSO)obj, req);
        }        
    }
}
