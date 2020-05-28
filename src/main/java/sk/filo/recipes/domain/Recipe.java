package sk.filo.recipes.domain;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.repository.UserRepository;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString(exclude = "associatedRecipes")
@Entity
@Table(name = "cb_recipe")
@SequenceGenerator(name = "recipe_generator", allocationSize = 1, sequenceName = "cb_recipe_seq")
public class Recipe {

    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
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

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id", nullable = false)
    @OrderBy("sortNumber ASC")
    private List<Section> sections;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinTable(name = "cb_recipe_recipe",
            joinColumns = @JoinColumn(name = "recipe_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "associated_recipe_id", nullable = false))
    @OrderBy("title ASC")
    private List<Recipe> associatedRecipes;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinTable(name = "cb_recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false))
    @OrderBy("name ASC")
    private List<Category> categories;
    
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id", nullable = false)
    @OrderBy("url ASC")
    private List<Source> sources;
    
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "recipe_id")
    @OrderBy("id ASC")
    private List<Picture> pictures;
    
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
    
    public List<Section> getSections() {
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
    
    @PrePersist
    public void prePersist() {
        // remove diacritics
        titleSearch = Normalizer.normalize(title, Normalizer.Form.NFD);
        titleSearch = titleSearch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        titleSearch = titleSearch.toLowerCase();

        // set creator
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authenticatedUser = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));

        created = LocalDateTime.now();
        creator = authenticatedUser;
    }
 
    @PreUpdate
    public void preUpdate() {
        // remove diacritics
        titleSearch = Normalizer.normalize(title, Normalizer.Form.NFD);
        titleSearch = titleSearch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        titleSearch = titleSearch.toLowerCase();

        // set modifier
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authenticatedUser = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));

        modified = LocalDateTime.now();
        modifier = authenticatedUser;
    }
}
