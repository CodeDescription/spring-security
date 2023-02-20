package org.example.springsecurity.controller;

import org.example.springsecurity.domain.Role;
import org.example.springsecurity.domain.User;
import org.example.springsecurity.repos.UserRepo;
import org.hibernate.bytecode.enhance.internal.tracker.NoopCollectionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null){
            model.put("message", "User exists!");
            return "registration";
        }

        user.setEnabled(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return "redirect:/login";
    }
}
