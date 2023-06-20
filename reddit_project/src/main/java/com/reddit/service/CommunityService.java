package com.reddit.service;

import com.reddit.entity.Community;
import com.reddit.entity.User;
import com.reddit.repository.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommunityService {

    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    UserService userService;

    public void saveNewCommunity(Long userId, String communityName, String radio) {
        Community community=new Community();
        community.setCommunityName(communityName);
        User user=userService.getUserByID(userId);
        community.setOwnerId(user);
        if(radio.equals("restricted")){
            community.setIsPrivate(false);
            community.setIsRestrict(true);
        }
        else if(radio.equals("private")){
            community.setIsPrivate(true);
            community.setIsRestrict(false);
        }
        else{
            community.setIsPrivate(false);
            community.setIsRestrict(false);
        }
        communityRepository.save(community);
    }
}
