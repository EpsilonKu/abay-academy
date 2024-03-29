package kz.bitter.project.controllers;

import kz.bitter.project.entities.Chapters;
import kz.bitter.project.entities.Lessons;
import kz.bitter.project.entities.Users;
import kz.bitter.project.services.CourseService;
import kz.bitter.project.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
  @Autowired
  private UserService userService;

  @Autowired
  private CourseService courseService;

  @Value("${file.avatar.uploadPath}")
  private String uploadPath;

  @Value("${file.avatar.linkPath}")
  private String linkPath;

  @Value("${file.avatar.defaultAvaPath}")
  private String defaultAvaPath;

  @GetMapping(value = "/")
  @PreAuthorize("isAnonymous()")
  public String index(Model model) {

    model.addAttribute("gender", "");
    model.addAttribute("currentUser", getUserData());
    return "index";
  }

  @GetMapping(value = "/profile")
  @PreAuthorize("isAuthenticated()")
  public String profile(Model model) {
    model.addAttribute("currentUser", getUserData());
    return "user/profile";
  }

  @GetMapping(value = "/setting")
  @PreAuthorize("isAuthenticated()")
  public String setting(Model model) {
    model.addAttribute("currentUser", getUserData());
    return "user/settings";
  }

  @PostMapping(value = "/save-user-settings")
  public String saveUserAccount(@RequestParam(name = "user_id") String id,
      @RequestParam(name = "user_email") String userEmail,
      @RequestParam(name = "user_nickname") String userNickname,
      @RequestParam(name = "user_full_name") String userFullName) {
    Users user = userService.getUserById(Long.parseLong(id));
    if (user != null) {
      user.setEmail(userEmail);
      user.setUsername(userNickname);
      user.setName(userFullName);
      return userService.saveUser(user) != null ? "redirect:/setting" : "redirect:/profile";
    }
    return "redirect:/";
  }

  @GetMapping(value = "/explore")
  @PreAuthorize("isAuthenticated()")
  public String exploreCourse(Model model) {

    return "redirect:/courses/0";
  }

  @GetMapping(value = "/courses/{page}")
  @PreAuthorize("isAuthenticated()")
  public String exploreCourse(Model model, @PathVariable("page") int page) {
    model.addAttribute("allCourses", courseService.getCoursesByPage(page));
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("currentPage", page);
    return "user/explore";
  }

  @GetMapping(value = "/course/{id}")
  public String courseView(@PathVariable("id") Long id,
      Model model) {
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("currentCourse", courseService.getCourseById(id));
    return "user/course-view";
  }

  @GetMapping(value = "/course/view/{id}/{chapterId}/{lessonId}")
  public String courseLearn(@PathVariable Long id,
      @PathVariable Long chapterId,
      @PathVariable Long lessonId,
      Model model) {
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("currentCourse", courseService.getCourseById(id));
    List<Chapters> chaptersList = courseService.getChapterByCourseId(id);
    model.addAttribute("currentChapterList", chaptersList);
    model.addAttribute("currentChapter", chapterId);
    model.addAttribute("currentLesson", lessonId);
    List<List<Lessons>> lessonsList = new ArrayList<>();
    for (Chapters i : chaptersList) {
      List<Lessons> lessons = courseService.getLessonsByChapterId(i.getId());
      lessonsList.add(lessons);
    }
    model.addAttribute("currentLessonList", lessonsList);
    model.addAttribute("currentUser", getUserData());
    return "user/course-learn";
  }

  @GetMapping(value = "/search")
  public String search(Model model, @RequestParam(name = "key") String key) {
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("key", key);
    return "/resultSet";
  }

  @GetMapping(value = "/signUp")
  public String signUp(HttpSession session,
      @RequestParam(name = "first_name") String firstName,
      @RequestParam(name = "last_name") String lastName) {
    if (firstName != null && lastName != null) {
      session.setAttribute("firstName", firstName);
      session.setAttribute("lastName", lastName);
      return "signUp";
    }
    return "redirect:/";
  }

  @GetMapping(value = "/navBar")
  public String signUp(HttpSession session) {
    session.setAttribute("currentUser", getUserData());
    return "layout/navBar";
  }

  @PostMapping(value = "/signUp")
  public String toSignUp(HttpSession session,
      @RequestParam(name = "user_email") String email,
      @RequestParam(name = "user_nickname") String nickname,
      @RequestParam(name = "user_new_password") String newPassword,
      @RequestParam(name = "user_re_new_password") String reNewPassword) {
    System.out.println(session.getAttribute("gender"));

    String firstName = session.getAttribute("firstName") + "";
    String lastName = session.getAttribute("lastName") + "";
    Users newUser = new Users();

    newUser.setEmail(email);
    newUser.setUsername(nickname);
    newUser.setPassword(newPassword);
    newUser.setName(firstName + lastName);
    newUser.setId(null);
    newUser.setPfp(null);

    if (newPassword.equals(reNewPassword)) {
      userService.registerUser(newUser);
      return "redirect:/profile";
    }
    return "redirect:/";
  }

  private Users getUserData() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      User secUser = (User) authentication.getPrincipal();
      return userService.getUserByEmailorUsername(secUser.getUsername(), secUser.getUsername());
    }
    return null;
  }

  @PostMapping(value = "/uploadPfp")
  public String uploadAva(@RequestParam(name = "photo") MultipartFile file) {

    Users user = getUserData();

    if (file.getContentType() != null && user != null
        && (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png"))) {
      try {

        String filename = "pfp_" + DigestUtils.sha1Hex("pfp_" + user.getId());

        // byte[] bytes = ImageUtils.cropImageSquare(file.getBytes());
        byte[] bytes = file.getBytes();

        Path path = Paths.get(uploadPath + filename + ".jpg");
        Files.write(path, bytes);

        user.setPfp(filename);
        userService.saveUser(user);

        return "redirect:/setting";
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error on uploadPfP");
      }
    }
    return "redirect:/profile";
  }

  @GetMapping(value = "/viewPhoto/{url}", produces = { MediaType.IMAGE_JPEG_VALUE })
  @PreAuthorize("isAuthenticated()")
  public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException {
    InputStream in;
    try {

      ClassPathResource resource = new ClassPathResource(linkPath + url + ".jpg");
      in = resource.getInputStream();
    } catch (Exception e) {
      ClassPathResource resource = new ClassPathResource(defaultAvaPath);
      in = resource.getInputStream();
    }
    return IOUtils.toByteArray(in);
  }

  public Users getUserData() {
    Users userData = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      User secUser = (User) authentication.getPrincipal();
      userData = userService.findByLogin(secUser.getUsername());
    }
    return userData;
  }
}
