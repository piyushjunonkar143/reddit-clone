package com.reddit.controller;

import com.reddit.entity.Community;
import com.reddit.entity.User;
import com.reddit.repository.PostRepository;
import com.reddit.service.CommunityService;
import com.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CommunityController {
    @Autowired
    CommunityService communityService;
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;

    @GetMapping("/new-community")
    public String createNewCommunity(@RequestParam("userId") Long userId, Model model){
        model.addAttribute("userId",userId);
        model.addAttribute("community",new Community());
        return "create-community";
    }

    @PostMapping("/save_community")
    public String saveCommunity(Principal principal,@RequestParam("userId")Long userId, @ModelAttribute("community") Community community,
                                @RequestParam("communityType") String communityType,
                                Model model){
        if (communityService.isCommunityNameExists(community.getCommunityName())) {
            model.addAttribute("message", true);
            model.addAttribute("userId",userId);
            return "redirect:/new-community";
        }

        Community savedCommunity=communityService.saveNewCommunity(principal,community,communityType);
        model.addAttribute("community",savedCommunity);
        User owner =community.getOwnerId();
        model.addAttribute("user",owner);
        model.addAttribute("userId",owner.getUserId());
        return "edit-community";
    }

    @GetMapping("/view-community/{communityName}")
    public String viewCommunity(@PathVariable("communityName") String communityName, Model model,Principal principal){
        Community community = communityService.findCommunityByCommunityName(communityName);
        model.addAttribute("community",community);



        if(principal == null &&(community.getIsPrivate() ||community.getIsRestrict())){
            return "redirect:/home";
        }
        if(principal!=null){
            String username=principal.getName();
            if((community.getIsPrivate() || community.getIsRestrict()) && community.getOwnerId().getUsername() != username && !community.getCommunityMembers().contains(userService.getByUsername(username)))
            {
                return "redirect:/home";
            }
            User user=userService.getByUsername(principal.getName());
            model.addAttribute("userData",user);
        }
        model.addAttribute("postsData",
                postRepository.findCommunityPostsOrderByPublishedAt(
                                communityName,
                                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "publishedAt")))
                        .getContent()
        );
        return "community";
    }

    @GetMapping("/users/r")
    public String viewMembers(Principal principal,@RequestParam(value = "communityName") String communityName, Model model){
        User user = userService.getByUsername(principal.getName());
        Community community = communityService.findCommunityByCommunityName(communityName);
        boolean isModerator = communityService.isModerator(community,user);
        model.addAttribute("community",community);
        model.addAttribute("isModerator" , isModerator);
        if(user.getUsername() != community.getOwnerId().getUsername() && !isModerator){
            return "redirect:/view-community/" + communityName;
        }
        return "viewmembers";

    }
    @GetMapping("/community/addModerator")
    public String addMemberToModerator(@RequestParam("userId") Long userId ,
                                       @RequestParam("communityName") String communityName,
                                       Model model) {

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

    @PostMapping("/join-communtiy")
    public String joinCommunity(@RequestParam("communityName") String communityName,
                                Principal principal,
                                Model model) {
        String username = principal.getName();
        Community community = communityService.findCommunityByCommunityName(communityName);
        if (community.getOwnerId().getUsername().equals(username)) {
            return "redirect:/view-community/" + communityName;
        }
        boolean isMember = communityService.isMember(community.getCommunityName(), username);
        if (!isMember) {
            communityService.addMember(community.getCommunityName(), username);
        }
        return "redirect:/view-community/"+communityName;
    }

    @PostMapping("/remove-community")
    public String removeCommuntiy(@RequestParam("communityName") String communityName, Principal principal){
        String username = principal.getName();
        Community community = communityService.findCommunityByCommunityName(communityName);
        if(community.getOwnerId().getUsername().equals(username)){
            return "redirect:/view-community/" + communityName;
        }

        boolean isMember = communityService.isMember(community.getCommunityName(), username);
        if (isMember) {
            communityService.removeMember(community.getCommunityName(), username);
        }

        return "redirect:/view-community/"+communityName;
    }
    //anu
    @GetMapping("/view-settings")
    public String viewSettingPage(@RequestParam("communityName") String communityName, Model model){
        Community community = communityService.findCommunityByCommunityName(communityName);
        model.addAttribute("community", community);
        return "edit-community";
    }
    @GetMapping("/add-settings")
    public String addSettingsCommunity(@RequestParam(name = "about" , required = false) String about,
                                       @RequestParam("communityName") String communityName,
                                       @RequestParam("owner") Long userId, Model model){

        Community community = communityService.findCommunityByCommunityName(communityName);
        User user = userService.getUserByID(userId);
//        communityService.addSettingsOfCommunity(community,about);
        model.addAttribute("community",community);
        model.addAttribute("userData", user);
        model.addAttribute("userId",user.getUserId());
        return "community";
    }
}
