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
@Table(name = "cb_ingredient")
@SequenceGenerator(name = "ingredient_generator", allocationSize = 1, sequenceName = "cb_ingredient_seq")
public class Ingredient {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "ingredient_generator")
    private Long id;
    
    @Column(name = "sort_number")
    private Integer sortNumber;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "value", nullable = false)
    private Integer value;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;
}
