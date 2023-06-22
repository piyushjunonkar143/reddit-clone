package com.reddit.controller;


import com.reddit.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @RequestMapping(value = {"/", "/home"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String home(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {

        model.addAttribute("allPosts", homeService.homePagePostsGeneral(page, size).getContent());
        return "home-world";

        // if authenticated user we have to move him to home
    }

    @RequestMapping(value = {"/search/","/search"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String search(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         @RequestParam(value = "q", required = false) String searchWord,
                         @RequestParam(value = "type",required = false,defaultValue = "posts")String type,
                         Model model) {

        model.addAttribute("searchData", homeService.searchPagePosts(page,size,searchWord,type).getContent());
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("type", type);
        return "search-page";
    }
}
