package kz.bitter.project.controllers;

import kz.bitter.project.entities.Users;
import kz.bitter.project.enums.Gender;
import kz.bitter.project.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.linkPath}")
    private String linkPath;

    @Value("${file.avatar.defaultAvaPath}")
    private String defaultAvaPath;

    @GetMapping (value = "/")
    public String index (Model model){
        model.addAttribute("gender","");
        model.addAttribute("currentUser", getUserData());
        return "index";
    }

    @GetMapping (value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile (Model model){
        model.addAttribute("currentUser", getUserData());
        return "profile";
    }

    @GetMapping (value = "/search")
    public String search (Model model, @RequestParam (name = "key") String key){
        model.addAttribute("currenUser" , getUserData());

        model.addAttribute("key",key);
        return "/resultSet";
    }

    @GetMapping (value = "/signUp")
    public String signUp (Model model,
                          HttpSession session,
                          @RequestParam (name = "first_name") String firstName,
                          @RequestParam (name = "last_name" ) String lastName,
                          @RequestParam (name = "gender") String gender){
        if(firstName != null && lastName != null && gender != ""){
            session.setAttribute("firstName",firstName);
            session.setAttribute("lastName",lastName);
            session.setAttribute("gender",gender);
            return "/signUp";
        }
        return "redirect:/";
    }

    @PostMapping (value = "signUp")
    public String toSignUp (HttpSession session,
                            @RequestParam (name = "user_email") String email,
                            @RequestParam (name = "user_nickname") String nickname,
                            @RequestParam (name = "user_new_password") String newPassword,
                            @RequestParam (name = "user_re_new_password") String reNewPassword){
        System.out.println(session.getAttribute("gender"));
        if (newPassword.equals(reNewPassword)){
            String gender = session.getAttribute("gender") + "";
            if (gender.equals("male")){
                String firstName = session.getAttribute("firstName") + "";
                String lastName = session.getAttribute("lastName") + "";
                Users newUser = new Users();
                newUser.setEmail(email);
                newUser.setUsername(nickname);
                newUser.setPassword(newPassword);
                newUser.setName(firstName + lastName);
                newUser.setGender(Gender.MALE);
                newUser.setId(null);
                newUser.setBirthday(null);
                newUser.setAvatar(null);

                userService.registerUser(newUser);
                return "redirect:/profile";
            }
            if (session.getAttribute("gender") == "female"){
                String firstName = session.getAttribute("firstName") + "";
                String lastName = session.getAttribute("lastName") + "";

                Users newUser = new Users();
                newUser.setEmail(email);
                newUser.setUsername(nickname);
                newUser.setPassword(newPassword);
                newUser.setName(firstName + lastName);
                newUser.setGender(Gender.MALE);
                newUser.setId(null);
                newUser.setBirthday(null);
                newUser.setAvatar(null);

                userService.registerUser(newUser);
                return "/profile";
            }
        }
        return "redirect:/";
    }

    private Users getUserData (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmailorUsername(secUser.getUsername(),secUser.getUsername());
            return myUser;
        }
        return null;
    }

    @PostMapping (value = "/uploadava")
    public String uploadAva (@RequestParam (name = "avatar")MultipartFile file){

        Users user = getUserData();

        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
            try {

                String filename= DigestUtils.sha1Hex("avatar_" + user.getId());

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + filename + ".jpg");
                Files.write(path, bytes);

                user.setAvatar(filename);
                userService.saveUser(user);

                return "redirect:/profile?avasuccess";
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return "redirect:/profile?avaerror";
    }

    @GetMapping (value="/viewphoto/{url}" , produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewProfilePhoto (@PathVariable (name="url") String url) throws IOException {
        String pictureUrl = defaultAvaPath;

        InputStream in;

        try {

            ClassPathResource resource = new ClassPathResource(linkPath + url + ".jpg");
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(defaultAvaPath);
            in = resource.getInputStream();
        }
        return IOUtils.toByteArray(in);
    }
}
