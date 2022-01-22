package sk.filo.recipes.so;
        
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    
    @Size(max=160)
    private String description;
    
    @Min(1)
    @Max(15)
    private Integer serves;
    
    @Valid
    @NotEmpty
    private List<SectionSO> sections;

    private List<RecipeSimpleSO> associatedRecipes;
    
    private List<PictureBasicSO> pictures;
    
    @NotNull
    private Long category;
    
    private List<TagSO> tags;
    
    @Valid
    private List<SourceSO> sources;
    
    private UserBasicSO creator;
    
    private UserBasicSO modifier;
    
    private LocalDateTime created;
    
    private LocalDateTime modified;
    
    public List<SectionSO> getSections() {
        if (Objects.isNull(sections)) {
            sections = new ArrayList<>();
        }
        return sections;
    }
    
    public List<RecipeSimpleSO> getAssociatedRecipes() {
        if (Objects.isNull(associatedRecipes)) {
            associatedRecipes = new ArrayList<>();
        }
        return associatedRecipes;
    }
    
    public List<PictureBasicSO> getPictures() {
        if (Objects.isNull(pictures)) {
            pictures = new ArrayList<>();
        }
        return pictures;
    }
    
    public List<TagSO> getTags() {
        if (Objects.isNull(tags)) {
            tags = new ArrayList<>();
        }
        return tags;
    }
    
    public List<SourceSO> getSources() {
        if (Objects.isNull(sources)) {
            sources = new ArrayList<>();
        }
        return sources;
    }
}
