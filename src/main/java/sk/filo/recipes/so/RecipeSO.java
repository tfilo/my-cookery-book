package sk.filo.recipes.so;
        
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
public class RecipeSO {
    
    private Long id;
    
    @NotBlank
    @Size(max=80)
    private String title;
    
    @Size(max=255)
    private String description;
    
    @Valid
    @NotEmpty
    private List<SectionSO> sections;

    private List<Long> associatedRecipes;
    
    @NotEmpty
    private List<Long> categories;
    
    @Valid
    private List<SourceSO> sources;
    
    private String creator;
    
    private String modifier;
    
    private LocalDateTime created;
    
    private LocalDateTime modified;
    
    public List<SectionSO> getSections() {
        if (Objects.isNull(sections)) {
            sections = new ArrayList<>();
        }
        return sections;
    }

    public List<Long> getAssociatedRecipes() {
        if (Objects.isNull(associatedRecipes)) {
            associatedRecipes = new ArrayList<>();
        }
        return associatedRecipes;
    }
    
    public List<Long> getCategories() {
        if (Objects.isNull(categories)) {
            categories = new ArrayList<>();
        }
        return categories;
    }
    
    public List<SourceSO> getSources() {
        if (Objects.isNull(sources)) {
            sources = new ArrayList<>();
        }
        return sources;
    }
}
