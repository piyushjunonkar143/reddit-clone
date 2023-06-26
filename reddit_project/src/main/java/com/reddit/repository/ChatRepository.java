package com.reddit.repository;

import com.reddit.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Integer> {

    @Query("SELECT c FROM Chat c WHERE c.receiverName = :name")
    List<Chat> findByReceiverName(@Param("name") String name);
}
