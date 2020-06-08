package com.example.demo;

import com.example.demo.Repositories.InterestRepository;
import com.example.demo.Repositories.MessageRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.NewsService;
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

    @Autowired
    NewsService newsService;

    @Autowired
    InterestRepository interestRepository;

    @RequestMapping("/")
    public String index(Model model) {

        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        model.addAttribute("articles", newsService.articlesByCategory("General"));
        // this could be "Business", "Technology", "Entertainment", "General", "Health", "Science", "Sports"

        return "index";
    }

    // list all the posts and users who posted here
    @RequestMapping("/post")                //check????
    public String homePage(Model model) {
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        model.addAttribute("messages", messageRepository.findAll());
        return "posts";
    }

    // post inputing form
    @GetMapping("/add-post")
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
        return "addPost";
    }

    @PostMapping("/process-post")
    public String processMessage(@Valid@ModelAttribute("message") Message message,
                                 BindingResult result, Model model) {


        model.addAttribute("message", message);
        if(userService.getUser() != null) {
            String loggedUserName = userService.getUser().getFirstName() + " " + userService.getUser().getLastName();
            model.addAttribute("loggedUser", userService.getUser());
        }

        if (result.hasErrors()) {
            return "addPost";
        }

        messageRepository.save(message);
        return "redirect:/post";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "detail";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){

        messageRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/update-message/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        if(userService.getUser() != null){

            String tempLoggedUserName = userService.getUser().getFirstName() + " " + userService.getUser().getLastName();
            String tempMessageOwnerName = messageRepository.findById(id).get().getSentBy();
            if(!tempLoggedUserName.equals(tempMessageOwnerName)){
                return "canNotUpdate";
            }
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "addPost";
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
        if(tempUser.isEnabled()){
        tempUser.setEnabled(false);
        }
        else{
            tempUser.setEnabled(true);
        }
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

    @GetMapping("/guestRegister")
    public String guestRegisterationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("interests", interestRepository.findAll());
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "guestRegister";
    }

    @RequestMapping("/guestProcess")
    public String processRegisterationPage(@Valid @ModelAttribute("user") User user,
                                           BindingResult result, Model model) {
        model.addAttribute("user", user);
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }

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
    // to check the form grid structure is working
    @RequestMapping("/formTest")
    public String formTest(Model model) {
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }

        model.addAttribute("messages", messageRepository.findAll());
        return "formTest";
    }

    //to check if the navbar is working
    @GetMapping("/test")
    public String testPage(Model model){
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "test-navbar";
    }
    @GetMapping("/profile")
    public String loggedUserProfilePage(Model model){
        if(userService.getUser() != null){
            model.addAttribute("loggedUser", userService.getUser());
        }
        return "profile";
    }

}
