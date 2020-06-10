package sk.filo.recipes.so;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString
public class RecipeSearchCriteriaSO {
    
    public enum Direction {
        ASC, DESC;
    }
    
    public enum SortField {
        created, title;
    }
    
    private Long categoryId;
    
    private String title;
    
    private List<TagSO> tags;
    
    private Integer page = 0;
    
    private Integer pageSize = 16;
    
    private SortField sortField = SortField.title;
    
    private Direction direction = Direction.ASC;
    
    public String getTitleSearch() {
        if (title!=null) {
            String titleSearch = Normalizer.normalize(title, Normalizer.Form.NFD);
            titleSearch = titleSearch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            return titleSearch.toLowerCase().trim();
        }
        return null;
    }
    
    public PageRequest getPageRequest() {
        int _page, _pageSize;
        Sort.Direction _direction;
        String _sortField;
        
        if (page == null) {
            _page = 0;
        } else if (page < 0) {
            _page = 0;
        } else {
            _page = page;
        }

        if (pageSize == null) {
            _pageSize = 16;
        } else if (pageSize > 128) {
            _pageSize = 128;
        } else if (pageSize < 16) {
            _pageSize = 16;
        } else {
            _pageSize = pageSize;
        }
        
        switch (direction != null ? direction : Direction.ASC) {
            case ASC:
                _direction = Sort.Direction.ASC;
                break;
            case DESC:
                _direction = Sort.Direction.DESC;
                break;
            default:
                _direction = Sort.Direction.ASC;
                break;
        }
        
        switch (sortField != null ? sortField : SortField.title) {
            case created:
                _sortField = SortField.created.name();
                break;
            case title:
                _sortField = SortField.title.name();
                break;
            default:
                _sortField = SortField.title.name();
        }
        
        return PageRequest.of(_page, _pageSize, _direction, _sortField);
    }

    public List<TagSO> getTags() {
        if (Objects.isNull(tags)) {
            tags = new ArrayList<>();
        }
        return tags;
    }
}
