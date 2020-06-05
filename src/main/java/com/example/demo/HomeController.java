package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index(Model model) {

        model.addAttribute("messages", messageRepository.findAll());
        return "list";
//        return "index";
    }
    @GetMapping("/add-form")
    public String newMessage(Model model){
        Message messageWithUserName = new Message();
        if(userService.getUser() != null) {
            String loggedUserName = userService.getUser().getFirstName() + " " + userService.getUser().getLastName();
//            Message messageWithUserName = new Message();
            messageWithUserName.setSentBy(loggedUserName);
        }
        model.addAttribute("message", messageWithUserName);

        return "form";
    }

    @PostMapping("/process-form")
    public String processMessage(@Valid @ModelAttribute("message") Message message, BindingResult result, Model model) {

//        public String processMessage(@Valid @ModelAttribute("message") Message message,
//                @RequestParam("file") MultipartFile file, BindingResult result) {

        model.addAttribute("message", message);
        if(userService.getUser() != null) {
//            model.addAttribute("logged-user", userService.getUser());
        String loggedUserName = userService.getUser().getFirstName() + " " + userService.getUser().getLastName();
            System.out.println("line 56 HomeCntrl logged user first name is = " + loggedUserName);
//        String loggedUserName = userService.getUser().getLastName() + " " + userService.getUser().getFirstName();
//        message.setSentBy(loggedUserName);
        }

        if (result.hasErrors()) {

            return "redirect:/form";
        }


        messageRepository.save(message);
        return "redirect:/";

    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

//    @RequestMapping("/delete/{id}")
//    public String delMessage(@PathVariable("id") long id){
//        messageRepository.deleteById(id);
//        return "redirect:/";
//    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/course")
    public String course(Model model) {
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "course";
    }

    @RequestMapping("/teacher")
    public String teacher(Model model) {
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "teacher";
    }

    @RequestMapping("/student")
    public String student(Model model) {
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "student";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "admin";
    }

    // to see two different options of the logged user.
    // This method does not require having Principal in model.
    // It works by function call.
    @RequestMapping("/secure")
        public String secure(Principal principal, Model model) {
            String username = principal.getName();
            model.addAttribute("user", userRepository.findByUsername(username));
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
            return "secure";
        }

    @GetMapping("/register")
    public String showRegistrationPage(Principal principal, Model model) {
    model.addAttribute("newUser", new User());
        String username = principal.getName();
        model.addAttribute("loggedUser", userRepository.findByUsername(username));
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
    return "register";
    }

    @PostMapping("/processRegister")
    public String processRegistrationPage(@Valid @ModelAttribute("newUser") User user, BindingResult result, Principal principal, Model model) {
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }

        model.addAttribute("newUser", user);

    String username = principal.getName();
    model.addAttribute("loggedUser", userRepository.findByUsername(username));
    if(result.hasErrors()){
        user.clearPassword();
        return "register";
    }
    else{
        model.addAttribute("message", "User Account Created!");

        user.setEnabled(true);
        Role role = new Role(user.getUsername(), "ROLE_USER");
        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        roleRepository.save(role);
        userRepository.save(user);
        }
        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }


}
