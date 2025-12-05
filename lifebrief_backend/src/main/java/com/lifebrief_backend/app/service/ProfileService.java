package com.lifebrief_backend.app.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lifebrief_backend.app.entity.Profile;
import com.lifebrief_backend.app.repository.ProfileRepository;

@Service
public class ProfileService {
    private final ProfileRepository repository;

    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    public Profile create(Profile profile) {
        // Normalize input
        String username = profile.getUsername().trim();
        String email = profile.getEmail().trim();

        // Duplicate checks
        if (repository.findByUsernameIgnoreCase(username) != null) {
            throw new DuplicateUserException("Username already exists");
        }
        if (repository.findByEmailIgnoreCase(email) != null) {
            throw new DuplicateUserException("Email already exists");
        }

        // Assign normalized values
        profile.setUsername(username);
        profile.setEmail(email);

        // Generate UUID if not set
        if (profile.getProfileId() == null) {
            profile.setProfileId(UUID.randomUUID().toString());
        }

        return repository.save(profile);
    }

    public List<Profile> findAll() {
        return repository.findAll();
    }

    public Profile findByUsername(String username) {
        return repository.findByUsernameIgnoreCase(username.trim());
    }

    public Profile update(String id, Profile updated) {
        return repository.findById(id).map(p -> {
            p.setCity(updated.getCity());
            p.setState(updated.getState());
            p.setCountry(updated.getCountry());
            p.setAge(updated.getAge());
            p.setWeight(updated.getWeight());
            p.setFoodPreference(updated.getFoodPreference());
            p.setHealthProblems(updated.getHealthProblems());
            p.setFoodAllergies(updated.getFoodAllergies());
            p.setDaysNonVeg(updated.getDaysNonVeg());
            return repository.save(p);
        }).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}