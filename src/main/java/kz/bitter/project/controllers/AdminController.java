package kz.bitter.project.controllers;

import kz.bitter.project.entities.*;
import kz.bitter.project.services.CourseService;
import kz.bitter.project.services.EventService;
import kz.bitter.project.services.GroupService;
import kz.bitter.project.services.UserService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private EventService eventService;

    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private Date start;

    @GetMapping(value = "/user-panel")
    public String userPanel(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin/user-panel";
    }

    @GetMapping(value = "/course-panel")
    public String coursePanel(Model model) {
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "admin/course-panel";
    }

    @GetMapping(value = "/group-panel")
    public String groupPanel(Model model) {
        model.addAttribute("allGroups", groupService.getAllGroups());
        return "admin/group-panel";
    }

    @GetMapping(value = "/event-panel")
    public String eventPanel(Model model) {
        model.addAttribute("allEvents", eventService.getAllEvents());
        return "admin/event-panel";
    }



    @GetMapping(value = "/edit/group/{id}")
    public String editGroup(@PathVariable("id") Long id,
                             Model model) {
        model.addAttribute("userList", userService.getAllUsersByGroupId(id));
        model.addAttribute("courseList", groupService.getGroupById(id).getCourses());
        model.addAttribute("group", groupService.getGroupById(id));
        return "admin/edit-group";
    }


    @GetMapping(value = "/edit/course/{id}")
    public String editCourse(@PathVariable("id") Long id,
                             Model model) {
        model.addAttribute("chapterList", courseService.getChapterByCourseId(id));
        model.addAttribute("course", courseService.getCourseById(id));
        return "admin/edit-course";
    }

    @GetMapping(value = "/edit/chapter/{id}")
    public String editChapter(@PathVariable("id") Long id,
                              Model model) {
        model.addAttribute("lessonList", courseService.getLessonsByChapterId(id));
        model.addAttribute("chapter", courseService.getChapterById(id));
        return "admin/edit-chapter";
    }

    @GetMapping(value = "/edit/event/{id}")
    public String editEvent(Model model,
                             @PathVariable("id") Long id) {
        Events event = eventService.getEventById(id);
        model.addAttribute("event", event);
        model.addAttribute("groupList", event.getGroups());
        return "admin/edit-event";
    }
    @GetMapping(value = "/edit/lesson/{id}")
    public String editLesson(Model model,
                             @PathVariable("id") Long id) {

        Lessons lesson = courseService.getLessonbyId(id);
        model.addAttribute("currentLesson", lesson);
        return "admin/edit-lesson";
    }

    @PostMapping(value = "/save-course")
    public String saveCourse(
            @RequestParam(name = "course_id") Long id,
            @RequestParam(name = "course_name") String name,
            @RequestParam(name = "course_description") String description,
			@RequestParam(name = "course_reputation") int reputation) {
        Courses courses = new Courses();
        if (id != -1) {
            courses.setId(id);
        }
        courses.setName(name);
        courses.setDescription(description);
		courses.setReputation(reputation);

        courseService.saveCourse(courses);
        return "redirect:/course-panel";
    }

    @PostMapping(value = "/save-group")
    public String saveGroup(
            @RequestParam(name = "group_id") Long id,
            @RequestParam(name = "group_name") String name,
            @RequestParam(name = "group_description") String description) {
        Groups group = new Groups();
        if (id != -1) {
            group.setId(id);
        }
        group.setName(name);
        group.setDescription(description);

        groupService.saveGroups(group);
        return "redirect:/group-panel";
    }


    @PostMapping(value = "/save-event")
    public String saveEvent(
            @RequestParam(name = "event_id") Long id,
            @RequestParam(name = "event_name") String name,
            @RequestParam(name = "event_date")String sDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm-DD-yyyy", Locale.ENGLISH);
        try {
        Date date = formatter.parse(sDate);
        Events event = new Events();
            if (id != -1) {
                event.setId(id);
            }
            event.setName(name);
            event.setDate(date);
            eventService.saveEvent(event);
            return "redirect:/event-panel";
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/event-panel?error=true";
        }
    }
    @PostMapping(value = "/save-user-to-group")
    public String saveUserToGroup(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "group_id") Long groupId) {
            Users user = userService.getUserById(userId);
            Groups groups = groupService.getGroupById(groupId);
            userService.saveUserToGroup(user,groups);
        return "redirect:/edit/group/" + groupId;
    }

    @PostMapping(value = "/save-group-to-event")
    public String saveGroupToEvent(
            @RequestParam(name = "event_id") Long eventId,
            @RequestParam(name = "group_id") Long groupId) {
        Events event = eventService.getEventById(eventId);
        Groups group = groupService.getGroupById(groupId);
        eventService.saveGroupToEvent(group,event);
        return "redirect:/edit/event/" + eventId;
    }

    @PostMapping(value = "/save-course-to-group")
    public String saveCourseToGroup(
            @RequestParam(name = "course_id") Long courseId,
            @RequestParam(name = "group_id") Long groupId) {
        Courses courses = courseService.getCourseById(courseId);
        Groups groups = groupService.getGroupById(groupId);
        groupService.saveCourseToGroup(groups,courses);
        return "redirect:/edit/group/" + groupId;
    }

    @PostMapping(value = "/save-chapter")
    public String saveChapter(
            @RequestParam(name = "course_id") Long courseId,
            @RequestParam(name = "course_name") String name,
            @RequestParam(name = "course_description") String description,
            @RequestParam(name = "chapter_id") Long id) {
        Chapters chapter = new Chapters();
        if (id != -1) {
            chapter.setId(id);
        }
        Courses course = courseService.getCourseById(courseId);
        chapter.setCourse(course);
        chapter.setName(name);
        chapter.setDescription(description);

        courseService.saveChapter(chapter);
        return "redirect:/edit/course/" + courseId;
    }

    @PostMapping(value = "/save-lesson")
    public String saveLesson(Lessons lesson,
                             Model model,
                             @RequestParam(name = "chapter_id") Long chapterId) {
        lesson.setChapter(courseService.getChapterById(chapterId));
        courseService.saveLesson(lesson);
        return "redirect:/edit/chapter/" + lesson.getChapter().getId();
    }

    @PostMapping(value = "/save-account")
    public String saveUserAccount(@RequestParam(name = "user_id") String id,
                                  @RequestParam(name = "user_email") String userEmail,
                                  @RequestParam(name = "user_nickname") String userNickname,
                                  @RequestParam(name = "user_password") String password) {
        Users user = userService.getUserById(Long.parseLong(id));
        if (user != null) {
            user.setEmail(userEmail);
            user.setUsername(userNickname);
            return userService.saveUser(user) != null ? "redirect:/user-panel?saveSuccess="+id : "redirect:/user-panel?saveError=true";
        }
        return "redirect:/";
    }

    @PostMapping(value = "/new-lesson")
    public String saveLesson(@RequestParam(name = "chapter_id") Long chapterId) {
        Lessons lesson = new Lessons();
        lesson.setChapter(courseService.getChapterById(chapterId));
        lesson = courseService.saveLesson(lesson);
        return "redirect:/edit/lesson/" + lesson.getId();
    }

    @PostMapping(value = "/remove-course")
    public String removeCourse(
            @RequestParam(name = "course_id") Long id) {
        Courses course = courseService.getCourseById(id);
        if (course != null) {
            courseService.removeCourse(id);
            return "redirect:/course-panel?removeSuccess=" + id;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping(value = "/remove-group")
    public String removeGroup(
            @RequestParam(name = "group_id") Long id) {
        Groups group = groupService.getGroupById(id);
        if (group != null) {
            groupService.removeGroups(group);
            return "redirect:/group-panel?removeSuccess=" + id;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping(value = "/remove-event")
    public String removeEvent(
            @RequestParam(name = "event_id") Long id) {
        Events event = eventService.getEventById(id);
        if (event != null) {
            eventService.removeEvent(event);
            return "redirect:/event-panel?removeSuccess=" + id;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping(value = "/kick-user-from-group")
    public String kickUserFromGroup(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "group_id") Long groupId) {
        Groups groups = groupService.getGroupById(groupId);
        Users users = userService.getUserById(userId);
        userService.kickUserFromGroup(users,groups);
        return "redirect:/edit/group/" + groupId;
    }

    @PostMapping(value = "/kick-group-from-event")
    public String kickGroupFromEvent(
            @RequestParam(name = "event_id") Long eventId,
            @RequestParam(name = "group_id") Long groupId) {
        Groups groups = groupService.getGroupById(groupId);
        Events events = eventService.getEventById(eventId);
        eventService.kickGroupFromEvent(groups,events);
        return "redirect:/edit/event/" + eventId;
    }

    @PostMapping(value = "/kick-course-from-group")
    public String kickGroupFromGroup(
            @RequestParam(name = "course_id") Long courseId,
            @RequestParam(name = "group_id") Long groupId) {
        Groups groups = groupService.getGroupById(groupId);
        Courses courses = courseService.getCourseById(courseId);
        groupService.kickCourseFromGroup(groups,courses);
        return "redirect:/edit/group/" + groupId;
    }

    @PostMapping(value = "/remove-chapter")
    public String removeChapter(
            @RequestParam(name = "chapter_id") Long id,
            @RequestParam(name = "course_id") Long courseId) {
        Chapters chapter = courseService.getChapterById(id);
        if (chapter != null) {
            courseService.removeChapter(id);

            return "redirect:/edit/course/" + courseId;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping(value = "/remove-lesson")
    public String removeLesson(
            @RequestParam(name = "lesson_id") Long id,
            @RequestParam(name = "chapter_id") Long chapterId) {
        Lessons lesson = courseService.getLessonbyId(id);
        if (lesson != null) {
            courseService.removeLesson(id);
            return "redirect:/edit/chapter/" + chapterId;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping(value = "/remove-user")
    public String removeUser(
            @RequestParam(name = "user_id") Long id) {
        Users user = userService.getUserById(id);
        if (user != null) {
            userService.removeUserById(id);
            return "redirect:/user-panel?removeSuccess=" + id;
        }
        return "redirect:/user-panel?removeError=true";
    }

    @PostMapping(value = "/signUpFull")
    public String toSignUpFull(@RequestParam(name = "user_email") String email,
                               @RequestParam(name = "user_nickname") String nickname,
                               @RequestParam(name = "user_password") String password,
                               @RequestParam(name = "user_re_password") String rePassword,
                               @RequestParam(name = "first_name") String firstName,
                               @RequestParam(name = "last_name") String lastName) {
        Users newUser = new Users();

        newUser.setEmail(email);
        newUser.setUsername(nickname);
        newUser.setPassword(password);
        newUser.setName(firstName + lastName);
        newUser.setId(null);
        newUser.setAvatar(null);

        if (password.equals(rePassword)) {
            newUser = userService.registerUser(newUser);
            if(newUser != null) {
                return "redirect:/user-panel?regSuccess=" +newUser.getId();
            }
            else return "redirect:/user-panel?regNotFree=true";
        }
        return "redirect:/user-panel?regFail=true";
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
