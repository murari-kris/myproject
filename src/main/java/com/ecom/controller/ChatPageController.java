package com.ecom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

@Controller
public class ChatPageController {   // renamed for clarity

    @GetMapping("/chat/{username}")
    public String chatPage(@PathVariable String username, Model model) {
        model.addAttribute("chatWith", username);
        return "chat"; // loads chat.html
    }
}
