package sk.filo.recipes.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "cb_unit_category")
@SequenceGenerator(name = "unit_category_generator", allocationSize = 1, sequenceName = "cb_unit_category_seq")
public class UnitCategory {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "unit_category_generator")
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;

}
