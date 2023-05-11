package main;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Error implements ErrorController {
    @RequestMapping("/error")
    public String error(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
            int code = Integer.parseInt(status.toString());
            if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "500";
            }else if(code == HttpStatus.NOT_FOUND.value()){
                return "404";
            }
            return "403";
        }
        return "403";
    }
}
