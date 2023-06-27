package com.reddit.controller;

import com.reddit.dto.CommentDto;
import com.reddit.entity.Draft;
import com.reddit.entity.Post;
import com.reddit.entity.User;
import com.reddit.repository.UserRepository;
import com.reddit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class PostController {
    @Autowired
    FileService fileService;
    @Autowired
    PostService postService;
    @Autowired
    DraftController draftController;
    @Autowired
    DraftService draftService;
    @Autowired
    CommunityService communityService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Value("${project.image}")
    private String path;

    @GetMapping("/community-post")
    public String communityPost(@RequestParam(value = "communityName") String communityName, Model model) {
        model.addAttribute("communityName", communityName);
        model.addAttribute("communityList", communityService.findAllCommunities());
        return "NewPost";
    }

    @GetMapping("/new-post")
    public String viewPost(@RequestParam(value = "communityName", required = false) String communityName, Principal principal, Model model) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("draftedPosts", draftService.findAllDraftedPosts().size());
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("communityList", communityService.findAllCommunities());
        model.addAttribute("communityName", communityName);
        return "NewPost";
    }

    @PostMapping("/save-post")
    public String fileUpload(Principal principal,
                             @RequestParam(value = "userId", required = false) Long userId,
                             @RequestParam(value = "communityName", required = false) String communityName,
                             @RequestParam(value = "link draft", required = false) String linkDraft,
                             @RequestParam(value = "post-draft", required = false) String postDraft,
                             @RequestParam(value = "draft", required = false) String draft,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "image", required = false) List<MultipartFile> images,
                             @RequestParam(value = "content", required = false) String content,
                             @RequestParam(value = "url", required = false) String url,
                             @RequestParam(value = "poll", required = false) String poll,
                             @RequestParam(value = "update draft", required = false) String updateDraftLink,
                             @RequestParam(value = "cancel", required = false) String cancelButton,
                             @RequestParam(value = "draftId", required = false) UUID draftId,
                             Model model) throws IOException {
        if (cancelButton != null && (!cancelButton.isEmpty())) {
            return "redirect:/new-post";
        } else if (updateDraftLink != null && updateDraftLink.equals("update Draft")) {
            draftService.updateDraftLink(title, url, draftId, principal);
            return "redirect:/draft";
        } else if (linkDraft != null && linkDraft.equals("save Draft")) {
            draftService.draftPostUrl(title, url, principal);
            return "redirect:/draft";
        } else if (postDraft != null && postDraft.equals("Post")) {
            postService.saveDraftedPost(title, content, draftId, url, principal, communityName);
            draftService.deleteDraftById(draftId);
        } else if (draftId != null) {
            draftController.updateDraft(draftId, title, content, principal);
            return "redirect:/draft";
        } else if (draft != null && !draft.isEmpty()) {
            draftController.saveDraftPost(title, content, model, principal);
            List<Draft> draftPosts = draftService.findAllDraftedPosts();
            model.addAttribute("draftedPosts", draftPosts);
            return "draft";
        } else {
            postService.post(title, images, url, content, principal, communityName, path);
        }
        User user = userService.getUserByID(userId);
        model.addAttribute("user", user);
        return "UserProfile";
    }

    //yashavant
    @GetMapping("/media/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = fileService.load(path, filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/{typeOfAccount}/{username}/comments/{postId}")
    public String viewPost(@PathVariable Long postId,
                           @PathVariable String typeOfAccount,
                           @PathVariable String username,
                           Model model, Principal principal) {
        model.addAttribute("postData", postService.getPostByType(typeOfAccount, username, postId));
        model.addAttribute("commentDto", new CommentDto());
        if (principal != null) {
            model.addAttribute("loggedUserData", userRepository.findByUsernameIgnoreCase(principal.getName()).get());
        }
        return "view-post";
    }


    @GetMapping("/saved-posts")
    public String savedPosts(@RequestParam(value = "postId", required = false) Long postId, Principal principal, Model model) {
        List<Post> savedPostList = postService.savedPosts(postId, principal);
        return "redirect:/view-saved-posts";
    }

    @GetMapping("/view-saved-posts")
    public String viewSavedPosts(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("savedPosts", user.getSavedPosts());
        return "saved-posts";
    }

    @GetMapping("/t/{categoryName}")
    public String postByCategory(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 @PathVariable String categoryName, Model model) {

        Page<Post> pagePosts = postService.findPostsByCategory(page, size, categoryName);
        model.addAttribute("category", categoryName);
        model.addAttribute("feedType",null);
        model.addAttribute("allPosts",pagePosts.getContent());
        model.addAttribute("totalPagesCount",pagePosts.getTotalPages());
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        return "home-world";
    }
}