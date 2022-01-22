package sk.filo.recipes.domain;

import java.io.Serializable;
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
@Table(name = "cb_category")
@SequenceGenerator(name = "category_generator", allocationSize = 1, sequenceName = "cb_category_seq")
public class Category implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "category_generator")
    private Long id;

    @Column(name = "name", nullable = false, length = 80, unique = true)
    private String name;

}
