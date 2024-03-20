package by.sorface.sso.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/account/signin")
    public String index() {
        return "index";
    }

}
