package com.ecom.controller;

import com.ecom.model.User;
import com.ecom.service.UserService;
import com.ecom.util.ActiveUserStore;   // ✅ import this instead of SessionListener

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActiveUserStore activeUserStore;  // ✅ inject ActiveUserStore bean

    @GetMapping({"/home", "/"})
    public String homePage(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // ✅ Store in session
        session.setAttribute("username", username);

        // ✅ Add user to ActiveUserStore (online users)
        activeUserStore.addUser(username);

        User user = userService.findByUsername(username).orElse(new User());
        model.addAttribute("user", user);

        // ✅ Pass currently active users to view
        model.addAttribute("activeUsers", activeUserStore.getUsers());

        return "index"; // index.html
    }
}
