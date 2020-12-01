package kz.bitter.project.controllers;

import kz.bitter.project.entities.Courses;
import kz.bitter.project.entities.Users;
import kz.bitter.project.services.CourseService;
import kz.bitter.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/user-panel")
    public String userPanel (Model model){
        model.addAttribute("allUsers",userService.getAllUsers());
        return "admin/user-panel";
    }

    @GetMapping(value = "/course-panel")
    public String coursePanel (Model model){
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "admin/course-panel";
    }

    @PostMapping (value = "/saveCourse")

    public String saveCourse (
            @RequestParam (name = "course_id") String id,
            @RequestParam (name = "course_name") String name,
            @RequestParam (name = "course_description") String description){
        Courses courses = new Courses();
        try {
            courses.setId(Long.parseLong(id));
        }catch (Exception e){
            e.printStackTrace();
        }
        courses.setName(name);
        courses.setDescription(description);

        courseService.saveCourse(courses);
        return "redirect:/course-panel";
    }

    @PostMapping(value = "/saveAccount")
    public String saveUserAccount(@RequestParam(name = "user_id") String id,
                                  @RequestParam(name = "user_email") String userEmail,
                                  @RequestParam(name = "user_nickname") String userNickname,
                                  @RequestParam(name = "user_password") String password) {
        Users user = userService.getUserById(Long.parseLong(id));
        if (user != null ) {
            user.setEmail(userEmail);
            user.setUsername(userNickname);
            return userService.saveUser(user) != null ? "redirect:/user-panel" : "redirect:-/user-panel?saveerror";
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
