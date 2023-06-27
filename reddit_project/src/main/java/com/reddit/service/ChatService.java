package com.reddit.service;

import com.reddit.entity.Chat;
import com.reddit.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    ChatRepository chatRepository;
    public Chat saveMessage(String receiverName, String message, Principal principal) {
        Chat chat = new Chat();
        chat.setMessage(message);
        chat.setReceiverName(receiverName);
        chat.setSenderName(principal.getName());
       return chatRepository.save(chat);
    }

    public List<Chat> findChatByReceiverName(Principal principal) {
        return chatRepository.findByReceiverName(principal.getName());
    }

    public void deleteMessageById(Integer chatId) {
        chatRepository.deleteById(chatId);
    }
}
