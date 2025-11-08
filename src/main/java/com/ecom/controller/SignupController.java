package com.ecom.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecom.model.User;
import com.ecom.service.UserService;

@Controller
public class SignupController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public SignupController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // signup.html
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user) {
        // Save user with encoded password
        String rawPassword = user.getPassword();   // store raw password temporarily
        userService.registerUser(user);

        // Authenticate the user immediately after signup with raw password
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Redirect to home page (index.html)
        return "redirect:/";
    }
}
