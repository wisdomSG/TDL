package com.tdl.tdl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tdl/view")
public class AdminViewController {
    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin";
    }
    @GetMapping("/admin/post")
    public String adminPost() {
        return "adminpost";
    }
    @GetMapping("/admin/user")
    public String adminUser() {
        return "adminuser";
    }

    @GetMapping("/admin/user/post")
    public String adminUserPost(Model model ,@RequestParam("tagId") String tagId)  {
        model.addAttribute("tagId", tagId);
        return "admintempost";
    }
    @GetMapping("/admin/search")
    public String adminUserPost(Model model ,@RequestParam("tagId") String tagId , @RequestParam("option") String option)  {
        model.addAttribute("tagId", tagId);
        model.addAttribute("option", option);

        return "adminsearchpost";
    }


}
