package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }

        return "index";
    }

//    @RequestMapping("/")
//    public String index(Principal principal, Model model) {
//        String username = principal.getName();
//        model.addAttribute("loggedUser", userRepository.findByUsername(username));
//        return "index";
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
