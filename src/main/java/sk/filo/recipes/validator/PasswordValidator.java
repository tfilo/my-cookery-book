package sk.filo.recipes.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.so.UserSO;

/**
 *
 * @author tomas
 */
@Component
public class PasswordValidator implements Validator {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordValidator.class);
    

    @Override
    public boolean supports(Class clazz) {
        return UserSO.class.equals(clazz);
    }
    
    @Override
    public void validate(Object obj, Errors e) {
        UserSO user = (UserSO) obj;
        LOGGER.debug("validujem objekt user {}", user);
        if (user.getId() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(e, "password", "required");
            ValidationUtils.rejectIfEmptyOrWhitespace(e, "passwordConfirm", "required");
            passwordLength(user, e, true);
        } else {
            passwordLength(user, e, false);
        }
        passwordMatch(user, e);
    }
    
    private void passwordLength(UserSO user, Errors e, Boolean validateEmpty) {
        if (validateEmpty || !StringUtils.isEmptyOrWhitespace(user.getPassword())) { 
            if (user.getPassword().length() > 255 || user.getPassword().length() < 4) {
                e.rejectValue("password", "size", new Integer[]{4, 255}, null);
            }
        }
        if (validateEmpty || !StringUtils.isEmptyOrWhitespace(user.getPasswordConfirm())) {
            if (user.getPasswordConfirm().length() > 255 || user.getPasswordConfirm().length() < 4) {
                e.rejectValue("passwordConfirm", "size", new Integer[]{4, 255}, null);
            }
        }
    }
    
    private void passwordMatch(UserSO user, Errors e) {
        if (!StringUtils.equals(user.getPassword(), user.getPasswordConfirm()) ) {
            e.rejectValue("password", "notMatch", null);
            e.rejectValue("passwordConfirm", "notMatch", null);
        }
    }
}
