package sk.filo.recipes.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import sk.filo.recipes.domain.Recipe;
import sk.filo.recipes.so.RecipeSearchCriteriaSO;
import sk.filo.recipes.so.TagSO;

/**
 *
 * @author tomas
 */

public class RecipeSpecification implements Specification<Recipe> {

    private RecipeSearchCriteriaSO criteria;
    
    public RecipeSpecification(RecipeSearchCriteriaSO criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        criteriaQuery.distinct(true);
        
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(criteria.getTitleSearch())) {
            predicates.add(criteriaBuilder.like(root.get("titleSearch"), "%" + criteria.getTitleSearch() + "%"));
        }
        if (Objects.nonNull(criteria.getCategoryId())) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), criteria.getCategoryId()));
        }
        for (TagSO so : criteria.getTags()) {
            predicates.add(criteriaBuilder.equal(root.join("tags").get("id"), so.getId()));
        }

        return criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
    }
}
