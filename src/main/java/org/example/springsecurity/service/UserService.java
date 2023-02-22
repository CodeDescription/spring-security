package org.example.springsecurity.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.springsecurity.dto.UserAlreadyExistsException;
import org.example.springsecurity.dto.UserRegistrationDto;
import org.example.springsecurity.dto.UserUpdateDto;
import org.example.springsecurity.model.Role;
import org.example.springsecurity.model.User;
import org.example.springsecurity.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    public User save(User user) {
        return userRepo.save(user);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }
        user.setEnabled(true);
        user.setRoles(Collections.singleton(Role.ADMIN));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        userRepo.save(user);

//        sendMessage(user);
        return true;
    }

    public void updateProfile(User user, String password, String email) {
    String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setEmail(email);
            }
        }

        if (StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        user.setPassword(password);
        user.setEmail(email);

        userRepo.save(user);
    }

    public void updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepo.findByUsername(userUpdateDto.getUsername());
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setPassword(userUpdateDto.getPassword());
        userRepo.save(user);
    }

    public User registerUser(UserRegistrationDto userDto) {
        if (userRepo.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + userDto.getUsername() + " already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEnabled(true);
        user.setRoles(Collections.singleton(Role.USER));
        return userRepo.save(user);
    }

    public void updateUser(UserUpdateDto userUpdateDto) {
        User user = userRepo.findById(userUpdateDto.getId());

        if (user == null) {
            throw new EntityNotFoundException("User with id " + userUpdateDto.getId() + " not found");
        }

        user.setUsername(userUpdateDto.getUsername());
        user.setEmail(userUpdateDto.getEmail());
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());

        userRepo.save(user);
    }
}