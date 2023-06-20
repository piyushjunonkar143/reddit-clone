package com.reddit.service;

import com.reddit.entity.Draft;
import com.reddit.entity.User;
import com.reddit.repository.DraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DraftService {
    @Autowired
    DraftRepository draftRepository;
    @Autowired
    UserService userService;
    public void saveDraft(String title,String content,Long userId) {
        User user = userService.getUserByID(userId);
        Draft draft = new Draft();
        draft.setContent(content);
        draft.setTitle(title);
        draft.setUser(user);
        draftRepository.save(draft);
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

    public void updateDraftById(UUID draftId, String title, String content) {
        Draft existingDraft = draftRepository.findById(draftId).get();
        existingDraft.setTitle(title);
        existingDraft.setContent(content);
        draftRepository.save(existingDraft);
    }
}
