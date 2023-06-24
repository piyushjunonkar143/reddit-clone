package com.reddit.controller;

import com.reddit.service.NotificationService;
import com.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    UserService userService;

    @GetMapping("/notification-page")
    public String notificationPage(Model model, Principal principal) {
        model.addAttribute("notificationMessages",userService.getByUsername(principal.getName()).getNotifications());
        return "notification-page";
    }
/*
    @GetMapping("/notify/upvote")
    public String upvoteNotification(@RequestParam(value = "postId", required = false) Long postId, Long userId) {
        notificationService.saveUpvoteNotification(postId, 1L);
        return "redirect:/notification-page";
    }

    @GetMapping("/notify/downVote")
    public String downVoteNotification(@RequestParam(value = "postId", required = false) Long postId, Long userId) {
        notificationService.saveDownVoteNotification(postId, 1L);
        return "redirect:/notification-page";
    }*/

    @GetMapping("/delete/notification")
    public String deleteNotification(@RequestParam(value = "notificationId", required = false) Long notificationId) {
        notificationService.deleteByPostId(notificationId);
        return "redirect:/notification-page";
    }
}