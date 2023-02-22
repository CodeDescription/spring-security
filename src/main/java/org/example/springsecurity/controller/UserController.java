package org.example.springsecurity.controller;

import org.example.springsecurity.dto.UserRegistrationDto;
import org.example.springsecurity.dto.UserUpdateDto;
import org.example.springsecurity.model.Role;
import org.example.springsecurity.model.User;
import org.example.springsecurity.repos.UserRepo;
import org.example.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Principal principal, Pageable pageable, Model model) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        Page<User> users = userRepo.findAll(pageable);
        model.addAttribute("users", users);

        return "userList";
    }

    @GetMapping("/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userDto) {
        userService.registerUser(userDto);
        return "redirect:/login";
    }

    @PostMapping("/{userId}")
    public String updateUser(
            @PathVariable Long userId,
            @ModelAttribute("user") UserUpdateDto userDto,
            Model model
    ) {
        userService.updateUser(userId, userDto);
        model.addAttribute("message", "User has been updated successfully.");
        return "redirect:/user";
    }

    @PostMapping("/main")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            Model model
    ) {
        userService.updateProfile(user, password, email);
        model.addAttribute("message", "Profile has been updated successfully.");
        return "redirect:/user/profile";
    }

    @ModelAttribute("user")
    public UserUpdateDto userUpdateDto() {
        return new UserUpdateDto();
    }
}
