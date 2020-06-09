package sk.filo.recipes.controller.user;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.filo.recipes.controller.ModelAttributeConstants;
import sk.filo.recipes.service.TagService;
import sk.filo.recipes.so.TagSO;

/**
 *
 * @author tomas
 */
@Controller
@RequestMapping(value = "/tag")
public class TagController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TagController.class);
    
    @Autowired
    TagService tagService;
        
    private void setAllTags(final Model model) {
        model.addAttribute(ModelAttributeConstants.MODEL_TAGS, tagService.getAll());
    }
    
    @RequestMapping(value="/all")
    public String getTags(final Model model) {
        LOGGER.debug("Get all tags");
        setAllTags(model);
        return "fragments/tag::tagList";
    }
    
    @RequestMapping(value="/add")
    public String addTag(final Model model) {
        LOGGER.debug("Add tag action");
        model.addAttribute(ModelAttributeConstants.MODEL_TAG_SO, new TagSO());
        return "fragments/tag::tagForm";
    }
    
    @RequestMapping(value="/get/{id}")
    public String getTagById(final @PathVariable Long id, final Model model) {
        LOGGER.debug("Get tag by id {}", id);
        TagSO tag = tagService.get(id);
        model.addAttribute(ModelAttributeConstants.MODEL_TAG_SO, tag);
        return "fragments/tag::tagForm";
    }
    
    @RequestMapping(value="/save")
    public String saveTag(final Model model, final @Valid TagSO tagSO, final BindingResult bindingResult) {
        LOGGER.debug("Save tag action {}", tagSO);
        if (bindingResult.hasErrors()) {
            return "fragments/tag::tagForm";
        }
        tagService.save(tagSO);
        setAllTags(model);
        model.addAttribute(ModelAttributeConstants.RELOAD_TAGS, true);
        return "fragments/tag::tagList";
    }
    
    @RequestMapping(value="/delete/{tagId}")
    public String deleteTag(final Model model, final @PathVariable Long tagId) {
        LOGGER.debug("Delete tag action {}", tagId);
        tagService.delete(tagId);
        setAllTags(model);
        model.addAttribute(ModelAttributeConstants.RELOAD_TAGS, true);
        return "fragments/tag::tagList";
    }
    
    @RequestMapping(value = "/reloadTags")
    public String reloadTags(final Model model) {
        LOGGER.debug("Reloading tags");
        model.addAttribute(ModelAttributeConstants.MODEL_TAGS, tagService.getAll());
        return "home::tags";
    }
}
