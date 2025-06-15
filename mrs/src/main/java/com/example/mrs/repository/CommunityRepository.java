package com.example.mrs.repository;


import com.example.mrs.dto.CommunityWriteDTO;
import com.example.mrs.entity.Community;
import com.example.mrs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByCategory(String category);
}

