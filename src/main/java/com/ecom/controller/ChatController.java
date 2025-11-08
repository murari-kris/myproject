package com.ecom.controller;

import com.ecom.model.ChatMessage;
import com.ecom.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate, MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    // 🔹 Handle real-time private messages
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
        System.out.println("📩 Sending message from " + chatMessage.getSender() +
                " to " + chatMessage.getReceiver() +
                ": " + chatMessage.getContent());

        // ✅ Save message in DB
        messageRepository.save(chatMessage);

        // Send to receiver
        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver(),
                "/queue/messages",
                chatMessage
        );

        // Send back to sender so it appears in their chat
        messagingTemplate.convertAndSendToUser(
                chatMessage.getSender(),
                "/queue/messages",
                chatMessage
        );
    }

    // 🔹 REST API to fetch chat history between two users
    @GetMapping("/chat/history/{withUser}")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@PathVariable String withUser, Principal principal) {
        String currentUser = principal.getName();
        // Fetch messages between logged-in user & target user
        List<ChatMessage> messages = messageRepository.findBySenderAndReceiverOrderByTimestampAsc(currentUser, withUser);
        messages.addAll(messageRepository.findByReceiverAndSenderOrderByTimestampAsc(currentUser, withUser));
        messages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp())); // sort by time
        return messages;
    }
}
