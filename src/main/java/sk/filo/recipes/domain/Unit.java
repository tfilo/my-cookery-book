package sk.filo.recipes.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "cb_unit")
@SequenceGenerator(name = "unit_generator", allocationSize = 1, sequenceName = "cb_unit_seq")
public class Unit {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "unit_generator")
    private Long id;
    
    @Column(name = "name", nullable = false)
    private Integer name;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_category_id", nullable = false)
    private UnitCategory category;
}
