package sk.filo.recipes.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;
import sk.filo.recipes.so.UserBasicSO;
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
        if (obj instanceof UserSO) {
            UserSO user = (UserSO) obj;
            LOGGER.debug("Validating passwords in UserSO {}", user);
            if (user.getId() == null) {
                ValidationUtils.rejectIfEmptyOrWhitespace(e, "password", "required");
                ValidationUtils.rejectIfEmptyOrWhitespace(e, "passwordConfirm", "required");
                passwordLength(user.getPassword(), e, true);
            } else {
                passwordLength(user.getPassword(), e, false);
            }
            passwordMatch(user.getPassword(), user.getPasswordConfirm(), e);
            passwordStrength(user.getPassword(), e);
        } else if (obj instanceof UserBasicSO) {
            UserBasicSO user = (UserBasicSO) obj;
            LOGGER.debug("Validating passwords in UserBasicSO {}", user);
            passwordLength(user.getPassword(), e, false);
            passwordMatch(user.getPassword(), user.getPasswordConfirm(), e);
            passwordStrength(user.getPassword(), e);
        }
    }
    
    private void passwordLength(String password, Errors e, Boolean validateEmpty) {
        if (validateEmpty || !StringUtils.isEmptyOrWhitespace(password)) { 
            if (password.length() > 255 || password.length() < 8) {
                e.rejectValue("password", "size", new Integer[]{8, 255}, null);
            }
        }
    }
    
    private void passwordMatch(String password, String confirmPassword, Errors e) {
        if (!StringUtils.equals(password, confirmPassword) ) {
            e.rejectValue("password", "notMatch", null);
            e.rejectValue("passwordConfirm", "notMatch", null);
        }
    }
    
    private void passwordStrength(String password, Errors e) {
        if (!StringUtils.isEmptyOrWhitespace(password)) {
            if (!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*")) {
                e.rejectValue("password", "weak", null);
            }
        }
    }
}
