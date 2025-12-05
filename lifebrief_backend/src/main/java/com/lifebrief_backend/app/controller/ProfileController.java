package com.lifebrief_backend.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.entity.Profile;
import com.lifebrief_backend.app.service.ProfileService;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping
    public Profile create(@RequestBody Profile profile) {
        return service.create(profile);
    }

    @GetMapping
    public List<Profile> getAll() {
        return service.findAll();
    }

    @GetMapping("/{username}")
    public Profile getByUsername(@PathVariable String username) {
        return service.findByUsername(username);
    }

    @PutMapping("/{id}")
    public Profile update(@PathVariable String id, @RequestBody Profile profile) {
        return service.update(id, profile);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}