package sk.filo.recipes.domain;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@ToString(exclude = { "data", "thumbnail","recipe" })
@Entity
@Table(name = "cb_picture")
@SequenceGenerator(name = "picture_generator", allocationSize = 1, sequenceName = "cb_picture_seq")
public class Picture {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "picture_generator")
    private Long id;
    
    @Column(name = "title", nullable = true, length = 80)
    private String title;
    
    @Column(name = "uploaded", nullable = false)
    private LocalDateTime uploaded;
    
    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    private byte[] data;
    
    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    private byte[] thumbnail;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
