package com.automaticfactus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** GET / → sirve el formulario principal. */
    @GetMapping("/")
    public String home() {
        return "forward:/form.html";
    }
}
