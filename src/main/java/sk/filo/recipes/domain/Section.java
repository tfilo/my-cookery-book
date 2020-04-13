package sk.filo.recipes.domain;

import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "cb_section")
@SequenceGenerator(name = "section_generator", allocationSize = 1, sequenceName = "cb_section_seq")
public class Section {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "section_generator")
    private Long id;
    
    @Column(name = "sort_number")
    private Integer sortNumber;

    @Column(name = "name")
    private String name;
   
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "section_id", nullable = false)
    private List<Ingredient> ingredients;
    
    @Column(name = "method", nullable = false, length = 2000)
    private String method;
    
}
