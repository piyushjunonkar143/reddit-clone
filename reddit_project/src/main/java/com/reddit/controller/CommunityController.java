package com.reddit.controller;

import com.reddit.entity.Community;
import com.reddit.entity.User;
import com.reddit.service.CommunityService;
import com.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommunityController {
    @Autowired
    CommunityService communityService;
    @Autowired
    UserService userService;
    @GetMapping("/new-community")
    public String saveCommunity(@RequestParam("userId") Long userId, Model model){
        model.addAttribute("userId",userId);
        model.addAttribute("community",new Community());
        return "create-community";
    }

    @PostMapping("/save_community")
    public String saveCommunity(@RequestParam("userId")Long userId,@ModelAttribute("community") Community community,
                                @RequestParam("communityType") String communityType,
                                Model model){
        if (communityService.isCommunityNameExists(community.getCommunityName())) {
            model.addAttribute("message", true);
            model.addAttribute("userId",userId);
            return "redirect:/new-community";
        }

        Community savedCommunity=communityService.saveNewCommunity(userId,community,communityType);
        model.addAttribute("community",savedCommunity);
        return "community";
    }

    @GetMapping("/view-community/{communityName}")
    public String viewCommunity(@PathVariable("communityName") String communityName, Model model){
        Community community = communityService.findCommunityByCommunityName(communityName);
        model.addAttribute("community",community);
        return "community";
    }

    @GetMapping("/users/r")
    public String viewMembers(@RequestParam(value = "communityName") String communityName, Model model){
        Community community = communityService.findCommunityByCommunityName(communityName);
        model.addAttribute("community",community);
        return "viewmembers";

    }
    @GetMapping("/community/addModerator")
    public String addMemberToModerator(@RequestParam("userId") Long userId ,
                                       @RequestParam("communityName") String communityName,
                                       Model model)
    {

        Community community = communityService.findCommunityByCommunityName(communityName);
        User user = userService.getUserByID(userId);
        communityService.addMemberToModerator(community, user);
        userService.addModeratedCommunity(user, community);
        model.addAttribute("community",community);
        return "redirect:/users/r?communityName="+communityName;

    }
    @GetMapping("/community/removeModerator")
    public String removeMemberFromModerator(@RequestParam("userId") Long userId ,
                                            @RequestParam("communityName") String communityName,
                                            Model model) {
        Community community = communityService.findCommunityByCommunityName(communityName);
        User user = userService.getUserByID(userId);
        communityService.removeMemberFromModerator(community, user);
        userService.removeModeratedCommunity(user, community);
        model.addAttribute("community",community);
        return "redirect:/users/r?communityName="+communityName;

    }

    @GetMapping("/community/banUser")
    public String removeUserFromCommunity(@RequestParam("userId") Long userId,
                                          @RequestParam("communityName") String communityName,
                                          Model model) {

        Community community = communityService.findCommunityByCommunityName(communityName);
        User user = userService.getUserByID(userId);
        communityService.removeUserFromCommunity(community, user);
        model.addAttribute("community", community);
        return "redirect:/users/r?communityName="+communityName;
    }
}
