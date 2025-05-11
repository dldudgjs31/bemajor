package com.hong.bemajor.users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class UsersController {

    @GetMapping("/sign-in")
    public String signIn(Model model) {
        // fragments/header/sidebar/footer + ordersFragment 조합
        model.addAttribute("pageTitle", "로그인");
        return "pages/login/sign-in";
    }

}
