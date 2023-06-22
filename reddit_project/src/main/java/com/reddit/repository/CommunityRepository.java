package com.reddit.repository;

import com.reddit.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {
    Community findByCommunityName(String communityName);

    boolean existsByCommunityName(String communityName);

    Community findBycommunityName(String name);
}