package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index(Model model) {

        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }

        model.addAttribute("messages", messageRepository.findAll());

        return "list";

    }
    @GetMapping("/add-form")
    public String newMessage(Model model){
        Message messageWithUserName = new Message();
        if(userService.getUser() != null) {
            String loggedUserName = userService.getUser().getFirstName() + " " + userService.getUser().getLastName();

            messageWithUserName.setSentBy(loggedUserName);
        }
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        model.addAttribute("message", messageWithUserName);

        return "form";
    }

    @PostMapping("/process-form")
    public String processMessage(@Valid @ModelAttribute("message") Message message, BindingResult result, Model model) {


        model.addAttribute("message", message);
        if(userService.getUser() != null) {

        String loggedUserName = userService.getUser().getFirstName() + " " + userService.getUser().getLastName();

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
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "show";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/displayUsers")
    public String delMessage(Model model, Principal principal){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        model.addAttribute("allUsers", userRepository.findAll());


        return "admin";
    }

    @RequestMapping("/disable-user/{id}")
    public String disableUser(@PathVariable("id") long id, Model model, Principal principal){
        User tempUser = new User();
        tempUser = userRepository.findById(id).get();
        tempUser.setEnabled(false);
        userRepository.save(tempUser);
        model.addAttribute("allUsers", userRepository.findAll());
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }

        return "admin";
    }

    @RequestMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model, Principal principal){

        User toBeDeletedUser = userRepository.findById(id).get();

        String tempUsername = toBeDeletedUser.getFirstName() + " " + toBeDeletedUser.getLastName();


        for(Message msg : messageRepository.findAllBySentBy(tempUsername)){
            Long temp = msg.getId();
            messageRepository.deleteById(temp);
        }

        userRepository.deleteById(id);

        model.addAttribute("allUsers", userRepository.findAll());

//        chekc if the following is not repetition @both herea and also in "/disable-user"
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }

        return "admin";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
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
    @RequestMapping("/secure/{id}")
        public String secure(@PathVariable("id") long id, Principal principal, Model model) {
            model.addAttribute("user", userRepository.findById(id).get());
//        String username = principal.getName();
//            model.addAttribute("user", userRepository.findByUsername(username));
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
            return "secure";
        }


    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }

    @GetMapping("/guestRegister")
    public String guestRegisterationPage(Model model) {
        model.addAttribute("user", new User());
        return "guestRegister";
    }

    @RequestMapping("/guestProcess")
    public String processRegisterationPage(@Valid @ModelAttribute("user") User user,
                                           BindingResult result, Model model) {
        model.addAttribute("user", user);

        if(result.hasErrors()) {
            user.clearPassword();
            return "guestRegister";
        }
        else {
            model.addAttribute("message", "User Account Created");

            user.setEnabled(true);
            Role role = new Role(user.getUsername(), "ROLE_USER");
            Set<Role> roles = new HashSet<Role>();
            roles.add(role);

            roleRepository.save(role);
            userRepository.save((user));

        }

        return "redirect:/";
    }
}
