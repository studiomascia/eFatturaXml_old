package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.User;

public interface UserService {
    void save (User user);
    User findByUsername(String username);
}