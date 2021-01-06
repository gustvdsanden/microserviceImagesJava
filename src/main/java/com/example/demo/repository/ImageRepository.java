package com.example.demo.repository;

import com.example.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    @Query("select u from Image u WHERE u.key = :key")
    Image findImagesByKey(@Param("key") String key);
    @Query("select u from Image u WHERE u.userEmail = :email")
    Image findImagesByEmail(@Param("key") String email);
}
