package sk.filo.recipes.domain;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString(exclude = { "sections", "associatedRecipes", "tags", "sources", "pictures", "creator", "modifier" })
@Entity
@Table(name = "cb_recipe")
@SequenceGenerator(name = "recipe_generator", allocationSize = 1, sequenceName = "cb_recipe_seq")
public class Recipe {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "recipe_generator")
    private Long id;

    @Column(name = "title", nullable = false, length = 80)
    private String title;
    
    @Column(name = "title_search", nullable = false, length = 80)
    private String titleSearch;
    
    @Column(name = "description", length = 160)
    private String description;
    
    @Column(name = "serves")
    private Integer serves;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id", nullable = false)
    @OrderBy("sortNumber ASC")
    private final List<Section> sections = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinTable(name = "cb_recipe_recipe",
            joinColumns = @JoinColumn(name = "recipe_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "associated_recipe_id", nullable = false))
    @OrderBy("title ASC")
    private final List<Recipe> associatedRecipes = new ArrayList<>();
  
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinTable(name = "cb_recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false))
    @OrderBy("name ASC")
    private final List<Tag> tags = new ArrayList<>();
    
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id", nullable = false)
    @OrderBy("url ASC")
    private final List<Source> sources = new ArrayList<>();
    
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id")
    @OrderBy("id ASC")
    private final List<Picture> pictures = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "modifier_id", nullable = true)
    private User modifier;
    
    @Column(name = "modified", nullable = true)
    private LocalDateTime modified;
    
    @PrePersist
    public void prePersist() {
        // remove diacritics
        titleSearch = Normalizer.normalize(title, Normalizer.Form.NFD);
        titleSearch = titleSearch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        titleSearch = titleSearch.toLowerCase();
    }
 
    @PreUpdate
    public void preUpdate() {
        // remove diacritics
        titleSearch = Normalizer.normalize(title, Normalizer.Form.NFD);
        titleSearch = titleSearch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        titleSearch = titleSearch.toLowerCase();
    }
}
