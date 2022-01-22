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
@Table(name = "cb_source")
@SequenceGenerator(name = "source_generator", allocationSize = 1, sequenceName = "cb_source_seq")
public class Source {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "source_generator")
    private Long id;
    
    @Column(name = "url", nullable = false, length = 1000)
    private String url;
}
