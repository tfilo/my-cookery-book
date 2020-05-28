package sk.filo.recipes.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "cb_unit", 
       uniqueConstraints=@UniqueConstraint(columnNames={"name", "abbreviation"}))
@SequenceGenerator(name = "unit_generator", allocationSize = 1, sequenceName = "cb_unit_seq")
public class Unit {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "unit_generator")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 80)
    private String name;
    
    @Column(name = "abbreviation", nullable = false, length = 20)
    private String abbreviation;
    
    @Column(name = "value_required", nullable = false)
    private Boolean valueRequired;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "unit_category_id", nullable = false)
    private UnitCategory category;
}
