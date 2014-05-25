package com.gazecode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
 
 
@Controller
public class SpringSecurityHelloController {
 
	@RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "This page is publicly accessible. Viewing as default page.");
 
        return "public";
    }
	
    @RequestMapping("/public")
    public String accessPublicPage(Model model) {
        model.addAttribute("message", "This page is publicly accessible. No authentication is required to view.");
 
        return "public";
    }
     
    @RequestMapping("/secured/mypage")
    public String accessSecuredPage(Model model) {
        model.addAttribute("message", "Only you are authenticated and authorized to view this page.");
 
        return "/secured/mypage";
    }
}