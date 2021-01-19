package kz.bitter.project.controllers;

import kz.bitter.project.entities.Chapters;
import kz.bitter.project.entities.Courses;
import kz.bitter.project.entities.Lessons;
import kz.bitter.project.entities.Users;
import kz.bitter.project.services.CourseService;
import kz.bitter.project.services.UserService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

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

    @GetMapping(value = "/edit/course/{id}")
    public String editCourse (@PathVariable ("id") Long id,
                              Model model){
        model.addAttribute("chapterList", courseService.getChapterByCourseId(id));
        model.addAttribute("course",courseService.getCourseById(id));
        return "admin/edit-course";
    }

    @GetMapping(value = "/edit/chapter/{id}")
    public String editChapter (@PathVariable ("id") Long id,
                              Model model){
        model.addAttribute("lessonList", courseService.getLessonsByChapterId(id));
        model.addAttribute("chapter",courseService.getChapterById(id));
        return "admin/edit-chapter";
    }

    @GetMapping(value = "/edit/lesson/{id}")
    public String editLesson (Model model,
                              @PathVariable ("id") Long id){

        Lessons lesson = courseService.getLessonbyId(id);
        model.addAttribute("currentLesson",lesson);
        return "admin/edit-lesson";
    }

    @PostMapping (value = "/save-course")
    public String saveCourse (
            @RequestParam (name = "course_id") Long id,
            @RequestParam (name = "course_name") String name,
            @RequestParam (name = "course_description") String description){
        Courses courses = new Courses();
        if (id!= -1 ){
            courses.setId(id);
        }
        courses.setName(name);
        courses.setDescription(description);

        courseService.saveCourse(courses);
        return "redirect:/course-panel";
    }

    @PostMapping (value = "/save-chapter")
    public String saveChapter (
            @RequestParam (name = "course_id") Long courseId,
            @RequestParam (name = "course_name") String name,
            @RequestParam (name = "course_description") String description,
            @RequestParam (name = "chapter_id") Long id){
        Chapters chapter = new Chapters();
        if (id!= -1 ){
            chapter.setId(id);
        }
        Courses course = courseService.getCourseById(courseId);
        chapter.setCourse(course);
        chapter.setName(name);
        chapter.setDescription(description);

        courseService.saveChapter(chapter);
        return "redirect:/edit/course/" + courseId;
    }

    @PostMapping (value = "/save-lesson")
    public String saveLesson (Lessons lesson,
                              Model model,
                              @RequestParam(name = "chapter_id") Long chapterId){
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
        if (user != null ) {
            user.setEmail(userEmail);
            user.setUsername(userNickname);
            return userService.saveUser(user) != null ? "redirect:/user-panel" : "redirect:-/user-panel?saveerror";
        }
        return "redirect:/";
    }

    @PostMapping (value = "/new-lesson")
    public String saveLesson (@RequestParam(name = "chapter_id") Long chapterId){
        Lessons lesson = new Lessons();
        lesson.setChapter(courseService.getChapterById(chapterId));
        lesson = courseService.saveLesson(lesson);
        return "redirect:/edit/lesson/" + lesson.getChapter().getId();
    }

    @PostMapping (value = "/remove-course")
    public String removeCourse (
            @RequestParam (name = "course_id") Long id){
        Courses course = courseService.getCourseById(id);
        if(course != null) {
            courseService.removeCourse(id);
            return "redirect:/course-panel";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping (value = "/remove-chapter")
    public String removeChapter (
            @RequestParam (name = "chapter_id") Long id,
            @RequestParam (name = "course_id") Long courseId){
        Chapters chapter = courseService.getChapterById(id);
        if(chapter != null) {
            courseService.removeChapter(id);

            return "redirect:/edit/course/" + courseId;
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping (value = "/remove-lesson")
    public String removeLesson (
            @RequestParam (name = "lesson_id") Long id,
            @RequestParam (name = "chapter_id") Long chapterId){
        Lessons lesson = courseService.getLessonbyId(id);
        if(lesson != null) {
            courseService.removeLesson(id);
            return "redirect:/edit/chapter/" + chapterId;
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping (value = "/remove-user")
    public String removeUser (
            @RequestParam (name = "user_id") Long id){
        Users user = userService.getUserById(id);
        if (user !=null){
            userService.removeUserById(id);
        }
        return "redirect:/user-panel";
    }

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();

        return renderer.render(document);
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
