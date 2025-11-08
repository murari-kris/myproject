package com.ecom.repository;

import com.ecom.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiverOrderByTimestampAsc(String sender, String receiver);
    List<ChatMessage> findByReceiverAndSenderOrderByTimestampAsc(String receiver, String sender);
}
