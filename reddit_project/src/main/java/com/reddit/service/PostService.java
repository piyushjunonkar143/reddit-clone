package com.reddit.service;

import com.reddit.entity.Media;
import com.reddit.entity.Post;
import com.reddit.entity.User;
import com.reddit.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserService userService;
    public void savePostUrl(String title, String url,Long userId) {
        User user = userService.getUserByID(userId);
        Post post = new Post();
        post.setTitle(title);
        post.setUrl(url);
        post.setUser(user);
        postRepository.save(post);
    }

    public void savePostContent(String title, String content,Long userId) {
        User user = userService.getUserByID(userId);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        post.setIsPublished(true);
        postRepository.save(post);
    }

    public void saveMedia(String title, List<Media> savedMediaList,Long userId) {
        User user = userService.getUserByID(userId);
        Post post = new Post();
        post.setMediaList(savedMediaList);
        post.setTitle(title);
        post.setUser(user);
        post.setIsPublished(true);
        postRepository.save(post);
    }

    public void savePost(String title, List<Media> savedMediaList, String url, String content,Long userId) {
        User user = userService.getUserByID(userId);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUrl(url);
        post.setUser(user);
        post.setMediaList(savedMediaList);
        post.setIsPublished(true);
        postRepository.save(post);
    }

    //yashvant
    public Post getPostByType(String typeOfAccount, String username, Long postId) {
        if (typeOfAccount.equalsIgnoreCase("r")) {
            return postRepository.findPostsByCommunityName(username, postId).orElseThrow();
        }
        else if(typeOfAccount.equalsIgnoreCase("u")){
            System.out.println("user");
            return postRepository.findPostsByUsername(username, postId).orElseThrow();
        }
        return null;
    }


    public Post getPost(Long postId) {
        return postRepository.findById(postId).get();
    }
}
