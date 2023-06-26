package com.reddit.controller;

import com.reddit.entity.Community;
import com.reddit.entity.Post;
import com.reddit.entity.User;
import com.reddit.repository.PostRepository;
import com.reddit.service.CommunityService;
import com.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public String createNewCommunity(Model model){
        model.addAttribute("community",new Community());
        return "create-community";
    }

    @PostMapping("/save_community")
    public String saveCommunity(Principal principal, @ModelAttribute("community") Community community,
                                @RequestParam("communityType") String communityType,
                                Model model){
        if (communityService.isCommunityNameExists(community.getCommunityName())) {
            model.addAttribute("message", true);
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
    public String viewCommunity(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                @PathVariable("communityName") String communityName,
                                Model model,Principal principal
    ){
        Community community = communityService.findCommunityByCommunityName(communityName);
        model.addAttribute("community",community);
        if(principal!=null){
            User user=userService.getByUsername(principal.getName());
            model.addAttribute("userData",user);
        }

        Page<Post> communityPosts = postRepository.findCommunityPostsOrderByPublishedAt
                (communityName, PageRequest.of
                        (page > 1 ? page - 1 : 0, size, Sort.by(Sort.Direction.DESC, "publishedAt"))
                );

        model.addAttribute("allPosts",communityPosts.getContent());
        model.addAttribute("totalPagesCount",communityPosts.getTotalPages());
        model.addAttribute("page",page);
        model.addAttribute("size",size);
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
                                       @RequestParam("communityCategory") String communityCategory,
                                       @RequestParam("owner") Long userId, Model model){

        Community community = communityService.findCommunityByCommunityName(communityName);
        User user = userService.getUserByID(userId);
        communityService.addSettingsOfCommunity(community,about,communityCategory);
        return "redirect:/view-community/"+communityName;
    }
}
