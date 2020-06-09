package sk.filo.recipes.so;
        
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
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
    
    @Valid
    @NotEmpty
    private final List<SectionSO> sections = new ArrayList<>();

    private final List<RecipeSimpleSO> associatedRecipes = new ArrayList<>();
    
    private final List<PictureBasicSO> pictures = new ArrayList<>();
    
    @NotNull
    private Long category;
    
    private final List<TagSO> tags = new ArrayList<>();
    
    @Valid
    private final List<SourceSO> sources = new ArrayList<>();
    
    private UserBasicSO creator;
    
    private UserBasicSO modifier;
    
    private LocalDateTime created;
    
    private LocalDateTime modified;
}
