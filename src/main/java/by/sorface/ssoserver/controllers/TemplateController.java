package by.sorface.ssoserver.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping({"/login", "/signup"})
    public String index() {
        return "index";
    }

}
