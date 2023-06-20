package com.reddit.controller;

import com.reddit.entity.Community;
import com.reddit.entity.User;
import com.reddit.service.CommunityService;
import com.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommunityController {
    @Autowired
    CommunityService communityService;
    @Autowired
    UserService userService;
    @GetMapping("/new-community")
    public String saveCommunity(@RequestParam("userId") Long userId, Model model){
        model.addAttribute("userId",userId);
        return "create-community";
    }

    @PostMapping("/save_community")
    public String saveCommunity(@RequestParam("userId")Long userId,@RequestParam("community_name") String communityName,@RequestParam("radio")String radio,Model model){
        System.out.println("communityName = "+communityName);
        System.out.println("radio = "+radio);
        System.out.println("userId = "+userId);
        communityService.saveNewCommunity(userId,communityName,radio);
        User user=userService.getUserByID(userId);
        model.addAttribute("user",user);
        return "UserProfile";
    }
}
