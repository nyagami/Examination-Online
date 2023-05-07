package main;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Error implements ErrorController {
    @RequestMapping("/error")
    public String error(HttpServletRequest request){
        return "404.html";
    }
}
