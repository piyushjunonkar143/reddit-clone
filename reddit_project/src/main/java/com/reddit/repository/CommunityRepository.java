package com.reddit.repository;

import com.reddit.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community,Long> {
    Community findByCommunityName(String communityName);

    boolean existsByCommunityName(String communityName);
}
