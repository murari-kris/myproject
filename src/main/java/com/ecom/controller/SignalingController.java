package com.ecom.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    public SignalingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Listen to messages from /app/signal and broadcast to /topic/signal
    @MessageMapping("/signal")
    public void signaling(@Payload String signalData) {
        System.out.println("📡 Received signaling message: " + signalData);
        messagingTemplate.convertAndSend("/topic/signal", signalData);
    }

}
