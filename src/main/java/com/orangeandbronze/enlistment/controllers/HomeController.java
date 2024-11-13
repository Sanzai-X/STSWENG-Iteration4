package com.orangeandbronze.enlistment.controllers;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.*;

@Controller
@RequestMapping("/")
class HomeController {

    @GetMapping
    RedirectView home() {
        return new RedirectView("login.html");
    }
}
