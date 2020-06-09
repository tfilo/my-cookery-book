package sk.filo.recipes.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString(exclude = "units")
@EqualsAndHashCode(exclude = "units")
@Entity
@Table(name = "cb_unit_category")
@SequenceGenerator(name = "unit_category_generator", allocationSize = 1, sequenceName = "cb_unit_category_seq")
public class UnitCategory {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "unit_category_generator")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 80, unique = true)
    private String name;
    
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    @OrderBy("name ASC")
    private final List<Unit> units = new ArrayList<>();
}
