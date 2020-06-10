package sk.filo.recipes.so.view;
        
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString
public class RecipeViewSO {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private Integer serves;
    
    private List<SectionViewSO> sections;

    private List<RecipeViewSO> associatedRecipes;
    
    private List<PictureViewSO> pictures;

    private List<String> sources;
    
    private UserViewSO creator;
    
    private UserViewSO modifier;
    
    private LocalDateTime created;
    
    private LocalDateTime modified;
    
    public List<SectionViewSO> getSections() {
        if (Objects.isNull(sections)) {
            sections = new ArrayList<>();
        }
        return sections;
    }

    public List<RecipeViewSO> getAssociatedRecipes() {
        if (Objects.isNull(associatedRecipes)) {
            associatedRecipes = new ArrayList<>();
        }
        return associatedRecipes;
    }
    
    public List<PictureViewSO> getPictures() {
        if (Objects.isNull(pictures)) {
            pictures = new ArrayList<>();
        }
        return pictures;
    }
    
    public List<String> getSources() {
        if (Objects.isNull(sources)) {
            sources = new ArrayList<>();
        }
        return sources;
    }
}
