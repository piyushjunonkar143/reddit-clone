package com.reddit.controller;

import com.reddit.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/{accountType}/{username}/{postId}/upvote")
    public String postUpvote(@PathVariable("accountType") String accountType,
                                @PathVariable("username") String username,
                                @PathVariable("postId") Long postId){
        System.out.println("post -- up");
        voteService.postUpVote(postId);
        return "redirect:/"+accountType+'/'+username+"/comments/"+postId;
    }

    @PostMapping("/{accountType}/{username}/{postId}/down-vote")
    public String postDownVote(@PathVariable("accountType") String accountType,
                             @PathVariable("username") String username,
                             @PathVariable("postId") Long postId){
        System.out.println("post -- down");
        voteService.postDownVote(postId);
        return "redirect:/"+accountType+'/'+username+"/comments/"+postId;
    }

    @PostMapping("/{accountType}/{username}/{postId}/comment/{commentId}/upvote")
    public String commentUpvote(@PathVariable("accountType") String accountType,
                              @PathVariable("username") String username,
                              @PathVariable("postId") Long postId,
                              @PathVariable("commentId") Long commentId){
        voteService.commentUpVote(postId, commentId);
        return "redirect:/"+accountType+'/'+username+"/comments/"+postId;
    }

    @PostMapping("/{accountType}/{username}/{postId}/comment/{commentId}/down-vote")
    public String commentDownVote(@PathVariable("accountType") String accountType,
                                @PathVariable("username") String username,
                                @PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId){
        voteService.commentDownVote(postId, commentId);
        return "redirect:/"+accountType+'/'+username+"/comments/"+postId;
    }

    @PostMapping("/{accountType}/{username}/{postId}/comment/{commentId}/upvote-reply/{replyId}")
    public String replyUpvote(@PathVariable("accountType") String accountType,
                              @PathVariable("username") String username,
                              @PathVariable("postId") Long postId,
                              @PathVariable("commentId") Long commentId,
                              @PathVariable("replyId") Long replyId){

        voteService.replyUpVote(replyId, commentId);
        return "redirect:/"+accountType+'/'+username+"/comments/"+postId;
    }

    @PostMapping("/{accountType}/{username}/{postId}/comment/{commentId}/down-vote-reply/{replyId}")
    public String replyDownVote(@PathVariable("accountType") String accountType,
                                @PathVariable("username") String username,
                                @PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId,
                                @PathVariable ("replyId") Long replyId){
        voteService.replyDownVote(replyId, commentId);
        return "redirect:/"+accountType+'/'+username+"/comments/"+postId;
    }
}
