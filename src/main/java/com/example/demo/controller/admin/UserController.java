package com.example.demo.controller.admin;

import com.example.demo.domain.User;
import com.example.demo.service.UploadService;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class UserController {

    // DI: dependency injection
    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public void index(Model model) {
//        List<User> listUser = userService.handleFindAllUsers();
//        System.out.println(listUser);
    }

    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> listUser = userService.handleFindAllUsers();
        // send data
        model.addAttribute("listUser", listUser);
        // redirect to view
        return "admin/user/index";
    }

    @GetMapping("/admin/user/{id}")
    public String getUserDetailPage(@PathVariable Long id, Model model) {
        User  user = userService.handleFindUserById(id);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String createUser(@ModelAttribute("newUser") User user, @RequestParam("avatarFile") MultipartFile file) {
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(user.getPassword());

        user.setAvatar(avatar);
        user.setPassword(hashPassword);
        userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(@PathVariable Long id, Model model) {
        User currentUser = userService.handleFindUserById(id);
        model.addAttribute("currentUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String updateUser(@ModelAttribute("currentUser") User requestUser) {
        User currentUser = userService.handleFindUserById(requestUser.getId());
        if (currentUser != null) {
            currentUser.setFullName(requestUser.getFullName());
            currentUser.setAddress(requestUser.getAddress());
            currentUser.setPhone(requestUser.getPhone());

            userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        User currentUser = userService.handleFindUserById(id);
        model.addAttribute("requestUser", currentUser);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String deleteUser(@ModelAttribute("requestUser") User requestUser) {
        userService.handleDeleteUser(requestUser.getId());
        return "redirect:/admin/user";
    }
}
