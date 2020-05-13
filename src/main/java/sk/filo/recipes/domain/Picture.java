package sk.filo.recipes.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "cb_picture")
@SequenceGenerator(name = "unit_generator", allocationSize = 1, sequenceName = "cb_unit_seq")
public class Picture {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "unit_generator")
    private Long id;
    
    @Column(name = "name", nullable = true, length = 255)
    private String title;
    
    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    private byte[] data;

}
