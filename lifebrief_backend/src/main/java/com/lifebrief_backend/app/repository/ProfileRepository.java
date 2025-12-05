package com.lifebrief_backend.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifebrief_backend.app.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    Profile findByUsernameIgnoreCase(String username);
    Profile findByEmailIgnoreCase(String email);
}