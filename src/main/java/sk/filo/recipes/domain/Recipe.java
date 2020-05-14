package sk.filo.recipes.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
@ToString
@Entity
@Table(name = "cb_recipe")
@SequenceGenerator(name = "recipe_generator", allocationSize = 1, sequenceName = "cb_recipe_seq")
public class Recipe {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "category_generator")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;
    
    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id", nullable = false)
    private List<Section> sections;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "cb_recipe_recipe",
            joinColumns = @JoinColumn(name = "recipe_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "associated_recipe_id", nullable = false))
    private List<Recipe> associatedRecipes;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "cb_recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false))
    private List<Category> categories;
    
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id", nullable = false)
    private List<Source> sources;
    
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id", nullable = false)
    private List<Picture> pictures;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "modifier_id", nullable = true)
    private User modifier;
    
    @Column(name = "modified", nullable = true)
    private LocalDateTime modified;
    
    public List<Section> getSection() {
        if (Objects.isNull(sections)) {
            sections = new ArrayList<>();
        }
        return sections;
    }
    
    public List<Recipe> getAssociatedRecipes() {
        if (Objects.isNull(associatedRecipes)) {
            associatedRecipes = new ArrayList<>();
        }
        return associatedRecipes;
    }
    
    public List<Category> getCategories() {
        if (Objects.isNull(categories)) {
            categories = new ArrayList<>();
        }
        return categories;
    }
    
    public List<Source> getSources() {
        if (Objects.isNull(sources)) {
            sources = new ArrayList<>();
        }
        return sources;
    }
    
    public List<Picture> getPictures() {
        if (Objects.isNull(pictures)) {
            pictures = new ArrayList<>();
        }
        return pictures;
    }
}
