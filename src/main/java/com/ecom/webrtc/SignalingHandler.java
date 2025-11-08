package com.ecom.webrtc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalingHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> data = mapper.readValue(message.getPayload(), Map.class);
        String type = (String) data.get("type");

        if ("join".equals(type)) {
            String room = (String) data.get("room");
            rooms.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(session);
            session.getAttributes().put("room", room);
            return;
        }

        String room = (String) session.getAttributes().get("room");
        if (room == null) return;

        Set<WebSocketSession> peers = rooms.getOrDefault(room, Collections.emptySet());
        for (WebSocketSession peer : peers) {
            if (peer.isOpen() && peer != session) {
                peer.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String room = (String) session.getAttributes().get("room");
        if (room != null) {
            Set<WebSocketSession> peers = rooms.get(room);
            if (peers != null) {
                peers.remove(session);
                if (peers.isEmpty()) rooms.remove(room);
            }
        }
    }
}