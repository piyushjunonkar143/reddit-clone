package com.reddit.service;

import com.reddit.entity.Draft;
import com.reddit.entity.User;
import com.reddit.repository.DraftRepository;
import com.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DraftService {
    @Autowired
    DraftRepository draftRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    public void saveDraft(String title,String content,Long userId) {
        User user = userService.getUserByID(userId);
        Set<Draft> drafts = new HashSet<>();
        Draft draft = new Draft();
        draft.setContent(content);
        draft.setTitle(title);
        draft.setUser(user);
        drafts.add(draftRepository.save(draft));
        user.setUserDrafts(drafts);
    }

    public List<Draft> findAllDraftedPosts() {
        return draftRepository.findAll();
    }

    public void deleteDraftById(UUID draftId) {
        draftRepository.deleteById(draftId);
    }

    public Draft getDraftById(UUID draftId) {
        return draftRepository.findById(draftId).get();
    }

    public void updateDraftById(UUID draftId, String title, String content, Long userId) {
        User user = userRepository.findByUserId(userId);
        Set<Draft> existingDrafts = user.getUserDrafts();
        existingDrafts.remove(draftRepository.findById(draftId).get());
        Draft existingDraft = draftRepository.findById(draftId).get();
        existingDraft.setTitle(title);
        existingDraft.setContent(content);
        existingDrafts.add(draftRepository.save(existingDraft));
        user.setUserDrafts(existingDrafts);
        userRepository.save(user);
    }

    public void draftPostUrl(String title, String url, Long userId) {
        User user = userService.getUserByID(userId);
        Set<Draft> drafts = new HashSet<>();
        Draft draft = new Draft();
        draft.setTitle(title);
        draft.setDraftUrl(url);
        draft.setUser(user);
        drafts.add(draftRepository.save(draft));
        user.setUserDrafts(drafts);
    }

    public void updateDraftLink(String title, String url, UUID draftId, Long userId) {
        User user = userRepository.findByUserId(userId);
        Set<Draft> existingDrafts = user.getUserDrafts();
        existingDrafts.remove(draftRepository.findById(draftId).get());
        Draft existingDraft = draftRepository.findById(draftId).get();
        existingDraft.setTitle(title);
        existingDraft.setDraftUrl(url);
        existingDrafts.add(draftRepository.save(existingDraft));
        user.setUserDrafts(existingDrafts);
        userRepository.save(user);
    }
}
