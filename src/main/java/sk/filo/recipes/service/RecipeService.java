package sk.filo.recipes.service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.domain.Ingredient;
import sk.filo.recipes.domain.Picture;
import sk.filo.recipes.domain.Recipe;
import sk.filo.recipes.domain.RoleName;
import sk.filo.recipes.domain.Section;
import sk.filo.recipes.domain.Tag;
import sk.filo.recipes.domain.User;
import sk.filo.recipes.mapper.IngredientMapper;
import sk.filo.recipes.mapper.RecipeMapper;
import sk.filo.recipes.mapper.SectionMapper;
import sk.filo.recipes.repository.CategoryRepository;
import sk.filo.recipes.repository.PictureRepository;
import sk.filo.recipes.repository.RecipeRepository;
import sk.filo.recipes.repository.TagRepository;
import sk.filo.recipes.repository.UnitRepository;
import sk.filo.recipes.repository.UserRepository;
import sk.filo.recipes.specification.RecipeSpecification;
import sk.filo.recipes.so.IngredientSO;
import sk.filo.recipes.so.PictureBasicSO;
import sk.filo.recipes.so.RecipeBasicSO;
import sk.filo.recipes.so.RecipeSO;
import sk.filo.recipes.so.RecipeSearchCriteriaSO;
import sk.filo.recipes.so.RecipeSimpleSO;
import sk.filo.recipes.so.SectionSO;
import sk.filo.recipes.so.TagSO;
import sk.filo.recipes.so.view.RecipeViewSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class RecipeService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

    @Autowired
    private RecipeMapper recipeMapper;
    
    @Autowired
    private SectionMapper sectionMapper;
    
    @Autowired
    private IngredientMapper ingredientMapper;
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private PictureRepository pictureRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UnitRepository unitRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    MessageSource messageSource;

    @Transactional
    public RecipeSimpleSO save(RecipeSO recipeSO) {
        LOGGER.debug("save recipeSO {}", recipeSO);        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authenticatedUser = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));
        
        Recipe recipe;
        if (Objects.isNull(recipeSO.getId())) {
            recipe = recipeMapper.mapRecipeSOToRecipe(recipeSO);
            recipe.setCreated(LocalDateTime.now());
            recipe.setCreator(authenticatedUser);
        } else {
            recipe = recipeRepository.findById(recipeSO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe with id not found!"));
            if (!recipe.getCreator().getUsername().equals(username) && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(RoleName.ROLE_ADMIN.name()))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users can edit only own recipes!");
            }
            recipeMapper.mapRecipeSOToRecipe(recipeSO, recipe);
            recipe.setModified(LocalDateTime.now());
            recipe.setModifier(authenticatedUser);
        }
        
        recipe.setCategory(categoryRepository.findById(recipeSO.getCategory())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found!")));

        
        mapAssociatedRecipes(recipeSO.getAssociatedRecipes(), recipe.getAssociatedRecipes());
        mapSections(recipeSO.getSections(), recipe.getSections());
        mapTags(recipeSO.getTags(), recipe.getTags());
        mapPictures(recipeSO.getPictures(), recipe.getPictures());
        
        LOGGER.debug("save recipe {}", recipe);
        return recipeMapper.mapRecipeToRecipeSimpleSO(recipeRepository.save(recipe));
    }
    
    public void delete(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not found!"));
        
        if (!recipe.getCreator().getUsername().equals(username) && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(RoleName.ROLE_ADMIN.name()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users can delete only own recipes!");
        }
        
        if (recipeRepository.countByAssociatedRecipesId(id) == 0) {
            recipeRepository.deleteById(id);
        } else {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("recipe.delete.constraint");
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
    }
    
    public RecipeSO get(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not found!"));
        LOGGER.debug("get recipe {}", recipe);
        return recipeMapper.mapRecipeToRecipeSO(recipe);
    }
    
    public RecipeViewSO getView(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not found!"));
        LOGGER.debug("get view recipe {}", recipe);
        
        RecipeViewSO recipeViewSO = recipeMapper.mapRecipeToRecipeViewSO(recipe);
        
        recipe.getAssociatedRecipes().forEach((Recipe r) -> {
            // map only first level of asociated recipes
            recipeViewSO.getAssociatedRecipes().add(recipeMapper.mapRecipeToRecipeViewSO(r));
        });

        return recipeViewSO;
    }
    
    public RecipeSimpleSO getBasic(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not found!"));
        LOGGER.debug("get recipe {}", recipe);
        return recipeMapper.mapRecipeToRecipeSimpleSO(recipe);
    }

    public List<RecipeSimpleSO> findTop4RecipeSimpleByTitle(String title, Long id) {
        Sort sort = Sort.by(Sort.Order.asc("title"));
        
        if (title!=null) {
            String titleSearch = Normalizer.normalize(title, Normalizer.Form.NFD);
            titleSearch = titleSearch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            title = titleSearch.toLowerCase();
        }
        
        List<Recipe> recipes;
        if (id != null) {
            recipes = recipeRepository.findTop4ByTitleSearchIsContainingAndIdNot(title, id, sort);
        } else {
            recipes = recipeRepository.findTop4ByTitleSearchIsContaining(title, sort);
        }
        return recipeMapper.mapRecipeListToRecipeSimpleSOList(recipes);
    }
    
    public Page<RecipeBasicSO> getAllBasicByCriteria(RecipeSearchCriteriaSO criteria) {
        Page<Recipe> recipes;
        LOGGER.debug("Search by criteria {}", criteria);
        recipes = recipeRepository.findAll(new RecipeSpecification(criteria), criteria.getPageRequest());
        LOGGER.debug("Found recipes {}", recipes.getContent());
        return mapToRecipeBasicSO(recipes);
    }

    private Page<RecipeBasicSO> mapToRecipeBasicSO(Page<Recipe> recipes) {
        return recipes.map(
                (recipe) -> {
                    RecipeBasicSO so = recipeMapper.mapRecipeToRecipeBasicSO(recipe);
                    return so;
                }
        );
    }
    
    private void mapAssociatedRecipes(final List<RecipeSimpleSO> associatedRecipeSOs, final List<Recipe> associatedRecipes) {
        associatedRecipes.clear();
        associatedRecipeSOs.forEach((RecipeSimpleSO so) -> {
            associatedRecipes.add(recipeRepository.findById(so.getId())
                                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Associated recipe not found!")));
        });
    }

    private void mapSections(final List<SectionSO> sectionSOs, final List<Section> sections) {
        sections.clear();
        sectionSOs.forEach((SectionSO so) -> {
            Section s = sectionMapper.mapSectionSOToSection(so);
            so.getIngredients().forEach((IngredientSO iso) -> {
                Ingredient i = ingredientMapper.mapIngredientSOToIngredient(iso);
                i.setUnit(unitRepository.findById(iso.getUnitId())
                          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit not found!")));
                s.getIngredients().add(i);
            });
            sections.add(s);
        });
    }

    private void mapPictures(List<PictureBasicSO> picturesSO, List<Picture> pictures) {
        pictures.clear();
        picturesSO.forEach((PictureBasicSO so) -> {
            Picture picture = pictureRepository.findById(so.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Picture not found!"));
            picture.setTitle(so.getTitle());
            pictures.add(picture);
        });
    }
    
    private void mapTags(final List<TagSO> tagSOs, final List<Tag> tags) {
        tags.clear();
        tagSOs.forEach((TagSO so) -> {
            tags.add(tagRepository.findById(so.getId())
                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag not found!")));
        });
    }
}
