package com.tdl.tdl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tdl")
public class HomeController {

    @GetMapping("/user/login")
    public String loginPage() { return "login";}


    @GetMapping("/user/signup")
    public String signupPage() { return "signup";}
}
