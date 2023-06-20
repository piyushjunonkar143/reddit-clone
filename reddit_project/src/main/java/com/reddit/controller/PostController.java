package com.reddit.controller;

import com.reddit.entity.Draft;
import com.reddit.entity.Media;
import com.reddit.service.DraftService;
import com.reddit.service.FileService;
import com.reddit.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${project.image}")
    private String path;
    @GetMapping("/new-post")
    public String viewPost(@RequestParam("userId")Long userId,Model model){
        model.addAttribute("draftedPosts",draftService.findAllDraftedPosts().size());
        model.addAttribute("userId",userId);
        return "NewPost";
    }

    @PostMapping("/save-post")
    public String fileUpload(@RequestParam("userId") Long userId,
                             @RequestParam(value = "draft",required = false)String draft,
                             @RequestParam(value = "title")String title,
                             @RequestParam(value = "image",required = false) List<MultipartFile> images,
                             @RequestParam(value = "content",required = false) String content,
                             @RequestParam(value = "url",required = false) String url,
                             @RequestParam(value = "poll",required = false) String poll,
                             @RequestParam(value = "draftId",required = false) UUID draftId,
                             Model model){
        if(draftId!=null){
            System.out.println("1111111111");
            draftController.updateDraft(draftId,title,content);
            return "redirect:/draft";
        }
        else if(draft!=null && !draft.isEmpty()){
            System.out.println("22222222222");
            draftController.saveDraftPost(title,content,model,userId);
            List<Draft> draftPosts = draftService.findAllDraftedPosts();
            model.addAttribute("draftedPosts",draftPosts);
            return "draft";
        }
        else {
            System.out.println("3333333333");
            if ((images != null && (!images.isEmpty())) && (url != null && (!url.isEmpty())) && (content != null
                    && (!content.isEmpty()))) {
                System.out.println("44444444");
                List<Media> savedMediaList = fileService.uploadImage(path, images);
                postService.savePost(title, savedMediaList, url, content,userId);
                return "file-response";
            }
            else if (images != null && (!images.isEmpty())) {
                System.out.println("55555555");
                List<Media> savedMediaList = fileService.uploadImage(path, images);
                postService.saveMedia(title, savedMediaList,userId);
                return "file-response";
            }
           else  if (url != null && (!url.isEmpty())) {
                System.out.println("6666666666");
                postService.savePostUrl(title, url,userId);
                return "file-response";
            }
            else if (content != null && (!content.isEmpty())) {
                System.out.println("77777777777");
                postService.savePostContent(title, content,userId);
                return "file-response";
            }
        }
        return "file-response";
    }
}