package com.ecom.controller;

import com.ecom.model.User;
import com.ecom.service.UserService;
import com.ecom.util.ActiveUserStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;
    private final ActiveUserStore activeUserStore;

    public UserController(UserService userService, ActiveUserStore activeUserStore) {
        this.userService = userService;
        this.activeUserStore = activeUserStore;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        // ✅ Get current logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        // ✅ Fetch all users except admin & current user
        List<User> users = userService.getAllUsers()
                .stream()
                .filter(u -> !u.getUsername().equalsIgnoreCase("admin"))
                .filter(u -> !u.getUsername().equalsIgnoreCase(currentUser)) // exclude self
                .toList();

        // ✅ Fetch active users excluding current user
        Set<String> activeUsers = activeUserStore.getUsers()
                .stream()
                .filter(u -> !u.equalsIgnoreCase(currentUser)) // exclude self
                .collect(Collectors.toSet());

        model.addAttribute("users", users);
        model.addAttribute("activeUsers", activeUsers);

        return "users"; // users.html
    }
}
