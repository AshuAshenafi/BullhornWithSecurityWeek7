package com.example.demo;

import com.example.demo.Model.News.Interest;
import com.example.demo.Repositories.InterestRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
public class SpringSecurityJdbcDataSource {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJdbcDataSource.class, args);
    }

    @Autowired
    InterestRepository interestRepository;

    @Bean
    public CommandLineRunner run(UserRepository userRepository,
                                 RoleRepository roleRepository) throws Exception {
        return(String[] args) -> {



            // Add interest set for the first user "bart"
//            Set<Interest> listOfInterest = new HashSet<>();
//
//            Interest tempInterest = new Interest();
//            tempInterest.setName("business");
//
//            listOfInterest.add(tempInterest);
//            tempInterest.setName("entertainment");
//
//            listOfInterest.add(tempInterest);
//            tempInterest.setName("general");
//
//            listOfInterest.add(tempInterest);






            // Add interest set for the second user "admin"
//            Set<Interest> listOfInterest2 = new HashSet<>();
//
//            Interest tempInterest2 = new Interest();
//            tempInterest2.setName("general");
//
//            listOfInterest2.add(tempInterest2);
//            tempInterest2.setName("health");
//
//            listOfInterest.add(tempInterest2);
//            tempInterest2.setName("science");
//
//            listOfInterest2.add(tempInterest2);


            // Add interest set for the second user "admin"
//            Set<Interest> listOfInterest3 = new HashSet<>();
//
//            Interest tempInterest3 = new Interest();
//            tempInterest2.setName("general");
//
//            listOfInterest3.add(tempInterest3);
//            tempInterest3.setName("health");
//
//            listOfInterest.add(tempInterest3);
//            tempInterest3.setName("science");
//
//            listOfInterest3.add(tempInterest3);

            // insert first user hard code it

            Interest interest;
            List<String> interestList = new ArrayList<>(Arrays
                    .asList("business", "entertainment", "general", "health", "science",
                            "sports", "technology"));
            for (String s : interestList) {
                interest = new Interest();
                interest.setName(s);
                interestRepository.save(interest);
            }

            User user = new User("bart", "bart@domain.com", "bart",
                    "Bart", "Simpson", true);

            Role userRole = new Role("bart", "ROLE_USER");
//            for (Interest interests: interestRepository.findAll()){
//                user.getInterests().add(interests);
//            }
            int h = 0;
            for (Interest interests: interestRepository.findAll()){
                if(h < 4){
                    user.getInterests().add(interests);
                }
                h++;
            }
            userRepository.save(user);
            roleRepository.save(userRole);

            // second user hard code it
            User admin1 = new User("admin", "ted@domain.com", "admin",
                    "Teddy", "Bear", true);
            Role adminRole1 = new Role("admin", "ROLE_ADMIN");
            int i = 0;
            for (Interest interests: interestRepository.findAll()){
                if(i > 2 && i < 6){
                admin1.getInterests().add(interests);
                }
                i++;
            }

            userRepository.save(admin1);
            roleRepository.save(adminRole1);


            // insert third user
            User admin = new User("super", "super@domain.com", "super",
                    "Super", "Man", true);
            Role adminRole = new Role("super", "ROLE_ADMIN");
            int j = 0;
            for (Interest interests: interestRepository.findAll()){
                if(j > 4 && j < 7){
                    admin.getInterests().add(interests);
                }
                j++;
            }

            userRepository.save(admin);
            roleRepository.save(adminRole);

            Role adminRole2 = new Role("super", "ROLE_USER");
            roleRepository.save(adminRole2);



        };
    }

}
