package kz.bitter.project.services;

import kz.bitter.project.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

public interface UserService extends UserDetailsService {
    Users getUserByEmailorUsername (String email,String username);
    Users registerUser (Users newUser);
    Users saveUser (Users user);
    List<Users> getAllUsers ();
    Users getUserById (Long id);
}
