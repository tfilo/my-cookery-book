package sk.filo.recipes.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author tomas
 */
@Controller
public class CustomErrorController implements ErrorController  {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeViewController.class);
    
    @RequestMapping("/error")
    public String handleError(final Model model, HttpServletRequest request) {
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        model.addAttribute("message", message);
        return "error::error";
    }
 
    public String getErrorPath() {
        return "/error";
    }
}
