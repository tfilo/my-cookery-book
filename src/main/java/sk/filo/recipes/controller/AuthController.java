package sk.filo.recipes.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author tomas
 */
@Controller
public class AuthController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    
    @RequestMapping(value="/login")
    public String login(final Model model) {
        LOGGER.debug("Authentificating user");
        return "home";
    }
    
    @RequestMapping(value="/logout")
    public String logout(final Model model, final HttpServletRequest req) {
        LOGGER.debug("Logout user");
        if (req.getUserPrincipal() != null) {
            try {
                req.logout();
            } catch (ServletException ex) {
                LOGGER.debug("Logout", ex);
            }
        }
        return "redirect:home";
    }
}
