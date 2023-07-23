package com.tdl.tdl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String HomePage() { return "redirect:/tdl/post";}

    @GetMapping("/tdl/search")
    public String Search() {return "search";}

}
