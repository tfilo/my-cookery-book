package sk.filo.recipes.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.component.Search;
import sk.filo.recipes.controller.ModelAttributeConstants;
import sk.filo.recipes.service.CategoryService;
import sk.filo.recipes.service.PictureService;
import sk.filo.recipes.service.RecipeService;
import sk.filo.recipes.service.TagService;
import sk.filo.recipes.service.UnitCategoryService;
import sk.filo.recipes.service.UserService;
import sk.filo.recipes.so.CategorySO;
import sk.filo.recipes.so.IngredientSO;
import sk.filo.recipes.so.PictureSO;
import sk.filo.recipes.so.RecipeSimpleSO;
import sk.filo.recipes.so.RecipeSO;
import sk.filo.recipes.so.SectionSO;
import sk.filo.recipes.so.SourceSO;
import sk.filo.recipes.so.TagSO;
import sk.filo.recipes.so.UnitCategorySO;
import sk.filo.recipes.so.UserBasicSO;
import sk.filo.recipes.validator.IngredientValidator;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/recipe")
public class RecipeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);
    
    @Autowired
    IngredientValidator ingredientValidator;
    
    @Autowired
    RecipeService recipeService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    TagService tagService;
    
    @Autowired
    UnitCategoryService unitCategoryService;
    
    @Autowired
    PictureService pictureService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    Search search;

    @ModelAttribute(ModelAttributeConstants.MODEL_CATEGORIES)
    public List<CategorySO> Categories() {
        LOGGER.debug("categories");
        return categoryService.getAll();
    }
    
    @ModelAttribute(ModelAttributeConstants.MODEL_TAGS)
    public List<TagSO> Tags() {
        LOGGER.debug("tags");
        return tagService.getAll();
    }
    
    @ModelAttribute(ModelAttributeConstants.MODEL_UNIT_CATEGORIES_WITH_UNITS)
    public List<UnitCategorySO> allUnitCategoriesWithUnits() {
        LOGGER.debug("allUnitCategories");
        return unitCategoryService.getAll();
    }
        
    @RequestMapping(value="/add")
    public String addRecipe(final Model model, final HttpServletRequest req) {
        LOGGER.debug("Add recipe");
        RecipeSO recipeSO = new RecipeSO();
        SectionSO sectionSO = new SectionSO();
        sectionSO.setSortNumber(1); // set default sort number
        recipeSO.getSections().add(sectionSO);
        UserBasicSO basicUser = userService.getBasicByUsername(req.getUserPrincipal().getName());
        recipeSO.setCreator(basicUser);
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPE_SO, recipeSO);
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value="/edit/{recipeId}")
    public String editRecipe(final Model model, final @PathVariable Long recipeId, final HttpServletRequest req) {
        LOGGER.debug("Edit recipe by id {}", recipeId);
        RecipeSO recipeSO = recipeService.get(recipeId);
        LOGGER.debug("Loaded recipe {}", recipeSO);
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPE_SO, recipeSO);
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value = "/sourceAdd")
    public String addSource(final RecipeSO recipeSO) {
        LOGGER.debug("addRow source {}", recipeSO);
        if (recipeSO!=null) {
            recipeSO.getSources().add(new SourceSO());
        }
        return "fragments/recipe::recipeForm";
    }

    @RequestMapping(value = "/sourceRemove/{rowId}")
    public String removeSource(final RecipeSO recipeSO, final @PathVariable Integer rowId , HttpServletRequest req) {
        LOGGER.debug("removeRow source {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            recipeSO.getSources().remove(rowId.intValue());
        }
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value = "/associatedRecipe/add/{recipeId}")
    public String addAssociatedRecipe(final RecipeSO recipeSO, final @PathVariable Long recipeId) {
        LOGGER.debug("addAssociatedRecipe {}, {}", recipeSO, recipeId);
        if (recipeId!=null && recipeSO!=null) {
            RecipeSimpleSO so = recipeService.getBasic(recipeId);
            if (!recipeSO.getAssociatedRecipes().contains(so)) {
                recipeSO.getAssociatedRecipes().add(so);
            }
        }
        return "fragments/recipe::associatedRecipes";
    }

    @RequestMapping(value = "/associatedRecipe/remove/{rowId}")
    public String removeAssociatedRecipe(final RecipeSO recipeSO, final @PathVariable Integer rowId) {
        LOGGER.debug("removeAssociatedRecipe {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            recipeSO.getAssociatedRecipes().remove(rowId.intValue());
        }
        return "fragments/recipe::associatedRecipes";
    }
    
    @RequestMapping(value = "/tag/add/{tagId}")
    public String addTag(final RecipeSO recipeSO, final @PathVariable Long tagId) {
        LOGGER.debug("addTag {}, {}", recipeSO, tagId);
        if (tagId!=null && recipeSO!=null) {
            TagSO so = tagService.get(tagId);
            if (!recipeSO.getTags().contains(so)) {
                recipeSO.getTags().add(so);
            }
        }
        return "fragments/recipe::tags";
    }
    
    @RequestMapping(value = "/tag/remove/{rowId}")
    public String removeTag(final RecipeSO recipeSO, final @PathVariable Integer rowId) {
        LOGGER.debug("removeTag {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            recipeSO.getTags().remove(rowId.intValue());
        }
        return "fragments/recipe::tags";
    }
    
    @RequestMapping(value = "/sectionAdd")
    public String addSection(final RecipeSO recipeSO) {
        LOGGER.debug("addRow section {}", recipeSO);
        if (recipeSO!=null) {
            SectionSO sectionSO = new SectionSO();
            sectionSO.setSortNumber(recipeSO.getSections().size() + 1);
            recipeSO.getSections().add(sectionSO);
        }
        return "fragments/recipe::sections";
    }

    @RequestMapping(value = "/sectionRemove/{rowId}")
    public String removeSection(final RecipeSO recipeSO, final @PathVariable Integer rowId) {
        LOGGER.debug("removeRow section {}, {}", recipeSO, rowId);
        if (recipeSO != null) {
            if (recipeSO.getSections().size() > 1) {
                recipeSO.getSections().remove(rowId.intValue());
            }
            
            // fix sort numbers
            int sortNo = 1; 
            for (SectionSO sectionSO : recipeSO.getSections()) {
                sectionSO.setSortNumber(sortNo++);
            }
        }
        return "fragments/recipe::sections";
    }

    @RequestMapping(value = "/ingredientAdd/{sectionRowId}")
    public String addIngredient(final RecipeSO recipeSO, final @PathVariable Integer sectionRowId) {
        LOGGER.debug("addRow ingredient {}, {}", recipeSO, sectionRowId);
        if (recipeSO!=null) {
            IngredientSO ingredientSO = new IngredientSO();
            ingredientSO.setSortNumber(recipeSO.getSections().get(sectionRowId).getIngredients().size() + 1);
            recipeSO.getSections().get(sectionRowId).getIngredients().add(ingredientSO);
        }
        LOGGER.debug("new ingredient {}", recipeSO);
        return "fragments/recipe::sections";
    }

    @RequestMapping(value = "/ingredientRemove/{sectionRowId}/{rowId}")
    public String removeIngredient(final RecipeSO recipeSO, final @PathVariable Integer sectionRowId, final @PathVariable Integer rowId , final HttpServletRequest req) {
        LOGGER.debug("removeRow ingredient {}, {}, {}", recipeSO, sectionRowId, rowId);
        if (recipeSO != null) {
            recipeSO.getSections().get(sectionRowId).getIngredients().remove(rowId.intValue());
            
            // fix sort numbers
            int sortNo = 1; 
            for (IngredientSO ingredientSO : recipeSO.getSections().get(sectionRowId).getIngredients()) {
                ingredientSO.setSortNumber(sortNo++);
            }
        }
        return "fragments/recipe::sections";
    }
    
    @RequestMapping(value="/delete/{recipeId}")
    public String deleteRecipe(final Model model, final @PathVariable Long recipeId, final HttpServletRequest req) {
        LOGGER.debug("Delete recipe action {}", recipeId);
        recipeService.delete(recipeId);
        
        return search.backToCategory(model, req);
    }

    @RequestMapping(value="/editAsRecipe/{sectionRowId}")
    public String editSectionAsRecipe(final Model model, final @PathVariable Integer sectionRowId, final RecipeSO recipeSO, final BindingResult bindingResult, final HttpServletRequest req) {
        LOGGER.debug("Edit section as new recipe {}", recipeSO);
        
        RecipeSO newRecipe = new RecipeSO();
        UserBasicSO basicUser = userService.getBasicByUsername(req.getUserPrincipal().getName());
        recipeSO.setCreator(basicUser);
        SectionSO section = recipeSO.getSections().get(sectionRowId);
        section.setId(null);
        section.setSortNumber(1);
        for (IngredientSO so : section.getIngredients()) {
            so.setId(null);
        }
        newRecipe.setTitle(section.getName());
        newRecipe.getSections().add(section);
        
        HttpSession session = req.getSession();
        session.setAttribute(ModelAttributeConstants.MODEL_RECIPE_SO, recipeSO);
        session.setAttribute(ModelAttributeConstants.SECTION_AS_RECIPE_IDX, sectionRowId);
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPE_SO, newRecipe);
        model.addAttribute(ModelAttributeConstants.SECTION_AS_RECIPE, true);
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value="/backToEdited")
    public String backToEdited(final Model model, final HttpServletRequest req) {
        LOGGER.debug("Back to edited");
        HttpSession session = req.getSession();
        Object attribute = session.getAttribute(ModelAttributeConstants.MODEL_RECIPE_SO);
        session.removeAttribute(ModelAttributeConstants.MODEL_RECIPE_SO);
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPE_SO, attribute);
        return "fragments/recipe::recipeForm";
    }
    
    @RequestMapping(value="/saveAsRecipe")
    public String saveSectionAsRecipeAndBackToEdited(final Model model, final @Valid RecipeSO recipe, final BindingResult bindingResult, final HttpServletRequest req) {
        LOGGER.debug("Save recipe action {}", recipe);
        ingredientValidator.validate(recipe, bindingResult);
        if (bindingResult.hasErrors()) {
            LOGGER.debug(bindingResult.toString());
            model.addAttribute(ModelAttributeConstants.SECTION_AS_RECIPE, true);
            return "fragments/recipe::recipeForm";
        }
        RecipeSimpleSO saved = recipeService.save(recipe);
        
        HttpSession session = req.getSession();
        RecipeSO editedRecipe = (RecipeSO)session.getAttribute(ModelAttributeConstants.MODEL_RECIPE_SO);
        Integer sectionIdx = (Integer)session.getAttribute(ModelAttributeConstants.SECTION_AS_RECIPE_IDX);
        
        session.removeAttribute(ModelAttributeConstants.MODEL_RECIPE_SO);
        session.removeAttribute(ModelAttributeConstants.SECTION_AS_RECIPE_IDX);
        
        editedRecipe.getAssociatedRecipes().add(saved);
        
        editedRecipe.getSections().remove(sectionIdx.intValue());

        // fix sort numbers
        int sortNo = 1;
        for (SectionSO sectionSO : editedRecipe.getSections()) {
            sectionSO.setSortNumber(sortNo++);
        }
        
        if (editedRecipe.getSections().isEmpty()) {
            SectionSO sectionSO = new SectionSO();
            sectionSO.setSortNumber(editedRecipe.getSections().size() + 1);
            editedRecipe.getSections().add(sectionSO);
        }
        
        model.addAttribute(ModelAttributeConstants.MODEL_RECIPE_SO, editedRecipe);
        
        return "fragments/recipe::recipeForm";
    }
       
    @RequestMapping(value="/save")
    public String saveRecipe(final Model model, final @Valid RecipeSO recipe, final BindingResult bindingResult, final HttpServletRequest req) {
        LOGGER.debug("Save recipe action {}", recipe);
        ingredientValidator.validate(recipe, bindingResult);
        if (bindingResult.hasErrors()) {
            LOGGER.debug(bindingResult.toString());
            return "fragments/recipe::recipeForm";
        }
        recipeService.save(recipe);
        
        return search.backToCategory(model, req);
    }
    
    @RequestMapping(value="/filter")
    public String filterRecipes(final Model model, final String title, Long id) {
        LOGGER.debug("filterRecipes {}, {}", title);
        List<RecipeSimpleSO> filtered;
        if (StringUtils.isEmptyOrWhitespace(title)) {
            filtered = new ArrayList<>();
        } else {
            filtered = recipeService.findTop4RecipeSimpleByTitle(title, id);   
        }
        model.addAttribute(ModelAttributeConstants.MODEL_FILTERED_RECIPES, filtered);
        return "fragments/recipe::filteredRecipes";
    }

    @RequestMapping(value = "/picture/thumbnail/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPictureById(final @PathVariable Long id) {
        LOGGER.debug("Getting picture by id");
        PictureSO pictureSO= pictureService.getThumbnail(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
            
        return new ResponseEntity<>(pictureSO.getData(), headers, HttpStatus.CREATED);
    }

    // Upload picture to database temporarily - after saving recipe, remove all picture not linked to any repice
    @RequestMapping(value = "/picture/save", method = RequestMethod.POST)
    public String uploadPicture(final @RequestParam("picture") MultipartFile picture, final HttpServletRequest request, final RecipeSO recipeSO) throws IOException {
        LOGGER.debug("Upload file {} {}", picture.getName(), recipeSO);
        PictureSO so = new PictureSO();
        so.setTitle(picture.getOriginalFilename());
        so.setData(picture.getBytes());
        recipeSO.getPictures().add(pictureService.save(so));

        return "fragments/recipe::pictures";
    }
    
    // Remove only from object not from DB!!!  - after saving recipe, remove all picture not linked to any repice
    @RequestMapping(value = "/picture/remove/{rowId}")
    public String removePicture(final RecipeSO recipeSO, @PathVariable Integer rowId) {
        LOGGER.debug("removePicture rowId {} {}", rowId, recipeSO);
        if (recipeSO != null) {
            recipeSO.getPictures().remove(rowId.intValue());
        }
        return "fragments/recipe::pictures";
    }
}
