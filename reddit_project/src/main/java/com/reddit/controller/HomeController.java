package com.reddit.controller;


import com.reddit.Security.SecurityUserOAuth;
import com.reddit.entity.Post;
import com.reddit.entity.User;
import com.reddit.repository.UserRepository;
import com.reddit.service.HomeService;
import com.reddit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;
    private final UserRepository userRepository;
    private final UserService userService;


    @RequestMapping(value = {"/", "/home"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String home(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model, @AuthenticationPrincipal SecurityUserOAuth oauthUser,
                       Principal principal) {

        if (oauthUser != null) {
            boolean isNewUser = userService.isNewOauthUser(oauthUser);
            if (isNewUser) userService.registerOauthUser(oauthUser);
        }

        if (principal != null) {
            User user = userRepository.findByUsernameIgnoreCase(principal.getName()).orElseThrow();
            Page<Post> posts = homeService.homePagePostsLoggedIn(page, size, user);
            model.addAttribute("allPosts",posts.getContent());
            model.addAttribute("totalPagesCount",posts.getTotalPages());
            model.addAttribute("userData", user);
            model.addAttribute("feedType",null);
            model.addAttribute("page",page);
            model.addAttribute("size",size);
            model.addAttribute("category",null);
            return "home";
        }

        Page<Post> posts = homeService.homePagePostsGeneral(page, size);
        model.addAttribute("allPosts", posts.getContent());
        model.addAttribute("totalPagesCount",posts.getTotalPages());
        model.addAttribute("feedType",null);
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        model.addAttribute("category",null);
        return "home-world";
    }

    @RequestMapping(value = {"/search/", "/search"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String search(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         @RequestParam(value = "q", required = false) String searchWord,
                         @RequestParam(value = "type", required = false, defaultValue = "posts") String type,
                         Model model,Principal principal) {
        model.addAttribute("searchData", homeService.searchPagePosts(page, size, searchWord, type).getContent());
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("type", type);
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        if(principal!= null){
            User user = userRepository.findByUsernameIgnoreCase(principal.getName()).orElseThrow();
            model.addAttribute("userData",user);
        }
        return "search-page";
    }

    @RequestMapping("/feed/{feedType}")
    public String feedPopular(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @PathVariable String feedType, Model model, Principal principal) {

        if (principal == null){
            Page<Post> feeds = homeService.getFeeds(page, size, feedType);
            model.addAttribute("allPosts",feeds.getContent());
            model.addAttribute("feedType",feedType);
            model.addAttribute("page",page);
            model.addAttribute("size",size);
            model.addAttribute("totalPagesCount",feeds.getTotalPages());
            model.addAttribute("category",null);
            return "home-world";
        }

        User user = userRepository.findByUsernameIgnoreCase(principal.getName()).orElseThrow();
        Page<Post> feeds = homeService.getFeeds(page, size, feedType);
        model.addAttribute("allPosts", feeds.getContent());
        model.addAttribute("totalPagesCount",feeds.getTotalPages());
        model.addAttribute("userData", user);
        model.addAttribute("feedType",feedType);
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        model.addAttribute("category",null);
        return "home";
    }
}