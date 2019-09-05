package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository user_Repository;
    @Autowired
    private RoleRepository role_Repository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        System.out.println("password prima della codifica = "+ user.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println("password dopo la codifica     = "+ user.getPassword());
        user.setRoles(new HashSet<>(role_Repository.findAll()));
        user_Repository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return user_Repository.findByUsername(username);
    }
}