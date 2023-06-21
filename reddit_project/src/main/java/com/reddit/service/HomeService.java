package com.reddit.service;


import com.reddit.entity.Post;
import com.reddit.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final PostRepository postRepository;

    public void homePagePostsLogin(int pageNumber,int pageSize){

    }

    public Page<Post> homePagePostsGeneral(int pageNumber, int size){
        Pageable page = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Post> postsOrderByPublishedAt = postRepository.findPostsOrderByPublishedAt(page);
        for(Post post : postsOrderByPublishedAt.getContent()){
            System.out.println(post.getTitle());
        }
        return postsOrderByPublishedAt;
    }

    public void getPagePosts(Pageable page){

    }
}
