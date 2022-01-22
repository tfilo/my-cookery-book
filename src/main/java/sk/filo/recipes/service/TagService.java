package sk.filo.recipes.service;

import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.recipes.domain.Tag;
import sk.filo.recipes.mapper.TagMapper;
import sk.filo.recipes.repository.RecipeRepository;
import sk.filo.recipes.repository.TagRepository;
import sk.filo.recipes.so.TagSO;

/**
 *
 * @author tomas
 */
@Service
@Transactional
public class TagService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TagService.class);

    @Autowired
    private TagMapper tagMapper;
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    MessageSource messageSource;
    
    @Transactional
    public void save(TagSO tagSO) {
        LOGGER.debug("save TagSO {}", tagSO);
        
        Tag tag = tagRepository.findByName(tagSO.getName());
        if (tag == null) {
            tagRepository.save(tagMapper.mapTagSOToTag(tagSO));
        } else {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("tag.add.constraint");
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
    }
    
    public void delete(Long id) {
        if (recipeRepository.countByTagsId(id) == 0) {
            tagRepository.deleteById(id);
        } else {
            MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
            String message = accessor.getMessage("tag.delete.constraint");
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
    }
    
    public List<TagSO> getAll() {
        Sort sort = Sort.by(Sort.Order.asc("name"));
        List<Tag> allTags = tagRepository.findAll(sort);
        return tagMapper.mapTagListToTagSOList(allTags);
    }
    
    public TagSO get(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag not found!"));
        return tagMapper.mapTagToTagSO(tag);
    }
}
