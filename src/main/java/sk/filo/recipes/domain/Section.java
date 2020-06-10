package sk.filo.recipes.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString(exclude = { "ingredients" })
@Entity
@Table(name = "cb_section")
@TypeDef(
    name = "jsonb",
    typeClass = JsonBinaryType.class
)
@SequenceGenerator(name = "section_generator", allocationSize = 1, sequenceName = "cb_section_seq")
public class Section {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "section_generator")
    private Long id;
    
    @Column(name = "sort_number")
    private Integer sortNumber;

    @Column(name = "name", length = 80)
    private String name;
   
    @OneToMany(
        fetch = FetchType.LAZY,   
        cascade={CascadeType.PERSIST, CascadeType.MERGE,  CascadeType.REFRESH},
        orphanRemoval = true
    )
    @JoinColumn(name = "section_id", nullable = false)
    @OrderBy("sortNumber ASC")
    private final List<Ingredient> ingredients = new ArrayList<>();
    
    @Column(columnDefinition="TEXT", name = "method", nullable = false)
    private String method;
 
}
