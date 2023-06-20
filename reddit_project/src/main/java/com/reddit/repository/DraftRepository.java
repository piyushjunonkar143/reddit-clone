package com.reddit.repository;

import com.reddit.entity.Draft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DraftRepository extends JpaRepository<Draft, UUID> {
}
