package kz.bitter.project.controllers;

import kz.bitter.project.entities.Users;
import kz.bitter.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/saveAccount")
    public String saveUserAccount(@RequestParam(name = "user_id") String id,
                                  @RequestParam(name = "user_email") String userEmail,
                                  @RequestParam(name = "user_nickname") String userNickname,
                                  @RequestParam(name = "user_password") String password) {
        Users user = userService.getUserById(Long.parseLong(id));
        if (user != null ) {
            user.setEmail(userEmail);
            user.setUsername(userNickname);
            return userService.saveUser(user) != null ? "redirect:/user-panel" : "redirect:/user-panel?saveerror";
        }
        return "redirect:/";
    }

    private Users getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmailorUsername(secUser.getUsername(), secUser.getUsername());
            return myUser;
        }
        return null;
    }
}
