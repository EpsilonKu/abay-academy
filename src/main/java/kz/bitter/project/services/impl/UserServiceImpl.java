package kz.bitter.project.services.impl;

import kz.bitter.project.entities.Groups;
import kz.bitter.project.entities.Roles;
import kz.bitter.project.entities.Users;
import kz.bitter.project.repositories.UserRepository;
import kz.bitter.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@EnableWebSecurity
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Users getUserByEmailorUsername(String email, String username) {
        System.out.println(email);
        return userRepository.findByEmailOrUsername(email, username);
    }

    @Override
    public Users saveUserToGroup(Users users, Groups groups) {
        users.getGroups().add(groups);
        userRepository.save(users);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmailOrUsername(email,email);
        if (user == null) {
            log.error("Username not found in database");
            throw new UsernameNotFoundException("Username not found in database");
        } else {
            log.info("Username found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                authorities);
    }


    @Override
    public Users registerUser(Users newUser) {
        Users checkUser = userRepository.findByEmailOrUsername(newUser.getEmail(), newUser.getUsername());
        if (checkUser == null){
//            newUser.setRole(Roles.USER);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return userRepository.save(newUser);
        }
        return null;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Users> getAllUsersByGroupId(Long id) {
        return userRepository.findByGroupsId(id);
    }

    @Override
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void removeUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void kickUserFromGroup(Users users, Groups groups) {
        for (Groups i:users.getGroups()){
            if (i == groups){
                users.getGroups().remove(i);
                break;
            }
        }
        userRepository.save(users);
    }

}
