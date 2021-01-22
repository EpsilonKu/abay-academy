package kz.bitter.project.services;

import kz.bitter.project.entities.Groups;
import kz.bitter.project.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

public interface UserService extends UserDetailsService {
    Users getUserByEmailorUsername (String email,String username);
    Users registerUser (Users newUser);
    Users saveUser (Users user);
    Users saveUserToGroup (Users users,Groups groups);

    void removeUserById (Long id);
    List<Users> getAllUsers ();
    List<Users> getAllUsersByGroupId (Long id);

    void kickUserFromGroup (Users users, Groups groups);

    Users getUserById (Long id);
}
