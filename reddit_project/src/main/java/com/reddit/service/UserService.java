package com.reddit.service;


import com.reddit.Security.SecurityUserOAuth;
import com.reddit.entity.Community;
import com.reddit.entity.User;
import com.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
     public void saveUser(User user){
         String displayName=user.getUsername().replaceAll(" ", "_");
         user.setDisplayName(displayName);
         user.setRoles("user");
         user.setKarma(1L);
         user.setPassword(encoder.encode(user.getPassword()));
         userRepository.save(user);
     }

    public User isUsernameAndPasswordCorrect(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User getUserByID(Long userId) {
         return userRepository.findById(userId).get();
    }
    public boolean saveUserPhoto(User user) throws IOException {
        try {
            if (user != null) {
                userRepository.save(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }

    public void addModeratedCommunity(User user, Community community) {
        Set<Community> moderatedCommunities = user.getCommunityModerators();
        moderatedCommunities.add(community);
        user.setCommunityModerators(moderatedCommunities);
        userRepository.save(user);
    }

    public void removeModeratedCommunity(User user, Community community) {
        Set<Community> moderatedCommunities = user.getCommunityModerators();
        moderatedCommunities.remove(community);
        user.setCommunityModerators(moderatedCommunities);
        userRepository.save(user);
    }

    public User getByUsername(String name) {
        return userRepository.findByUsername(name);
    }


    public boolean isNewOauthUser(SecurityUserOAuth user){
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(user.getUsername());
        return userOptional.isEmpty();
    }

    public void registerOauthUser(SecurityUserOAuth userDetails){
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setRoles("user");
        user.setDisplayName(userDetails.getUsername());
        user.setKarma(0L);
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
         return userRepository.findAll();
    }
}
