package com.reddit.controller;


import com.reddit.entity.Post;
import com.reddit.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {
    @GetMapping("/newPost")
    public String newPost(@RequestParam("username") User user, Model model){
        model.addAttribute("post",new Post());
        model.addAttribute("user",user);
        return "NewPost";
    }

    public void addPost(@ModelAttribute("user")User user, @ModelAttribute("post")Post post){

    }
}
