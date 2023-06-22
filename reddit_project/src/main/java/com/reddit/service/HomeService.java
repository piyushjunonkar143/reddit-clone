package com.reddit.service;


import com.reddit.entity.Community;
import com.reddit.entity.Post;
import com.reddit.repository.CommunityRepository;
import com.reddit.repository.PostRepository;
import com.reddit.repository.UserRepository;
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
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public void homePagePostsLogin(int pageNumber, int pageSize) {

    }

    public Page<Post> homePagePostsGeneral(int pageNumber, int size) {
        Pageable page = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Post> postsOrderByPublishedAt = postRepository.findPostsOrderByPublishedAt(page);
        for (Post post : postsOrderByPublishedAt.getContent()) {
            System.out.println(post.getTitle());
        }
        return postsOrderByPublishedAt;
    }

    public void getPagePosts(Pageable page) {

    }

    public Page<?> searchPagePosts(int pageNumber, int size, String keyword, String type) {
        if (type.equalsIgnoreCase("sr")) {
            return communityRepository.findCommunitiesBySearchName(keyword, PageRequest.of(pageNumber - 1, size));
        }
        if (type.equalsIgnoreCase("user")) {
            return userRepository.findUsersBySearchName(keyword, PageRequest.of(pageNumber - 1, size));
        }
        return postRepository.findPostsBySearchOrderByPublishedAt(keyword,
                PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "publishedAt")));
    }
}
