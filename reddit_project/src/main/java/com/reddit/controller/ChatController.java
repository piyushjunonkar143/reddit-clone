package com.reddit.controller;

import com.reddit.service.ChatService;
import com.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;
    @GetMapping("/chat")
    public String showChatPage(Model model,Principal principal){
        model.addAttribute("users",userService.findAllUsers());
        model.addAttribute("messages",chatService.findChatByReceiverName(principal));
        model.addAttribute("chats",principal.getName());
        return "chat";
    }

    @GetMapping("/send")
    public String sendChats(@RequestParam(value = "receiverName",required = false) String receiverName,@RequestParam(value = "message",required = false) String message,Principal principal, Model model){
      chatService.saveMessage(receiverName,message,principal);
       return "redirect:/chat";
    }

    @GetMapping("/delete-messages")
    public String deleteChats(@RequestParam(value = "chatId",required = false) Integer chatId){
        chatService.deleteMessageById(chatId);
        return "redirect:/chat";
    }
}
