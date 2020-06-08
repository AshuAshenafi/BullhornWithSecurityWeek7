package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

//@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/", "/guestRegister", "/guestProcess", "/error").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/displayUsers").hasRole("ADMIN")
//                .antMatchers("/register").hasRole("ADMIN")
                .antMatchers("/delete/**").hasRole("ADMIN")
                .antMatchers("/delete-admin/**").hasRole("ADMIN")
                .antMatchers("/disable-user/**").hasRole("ADMIN")
                .antMatchers("/delete-user/**").hasRole("ADMIN")
                .antMatchers("/secure/**").hasAnyRole("ADMIN", "USER")      // can go to profile page
                .antMatchers("/profile").hasAnyRole("ADMIN", "USER")
                .antMatchers("/formTest").permitAll()
                .antMatchers("/h2-console/**").permitAll()

                .antMatchers("/**").hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true").permitAll();

        //The following two line of code are mandatory to access
        // h2-console even if you are authorized.
        httpSecurity.csrf().ignoringAntMatchers("/h2-console/**");
        httpSecurity.headers().frameOptions().sameOrigin();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("SELECT username, password, enabled from " +
                    "users_db WHERE username=?")
            .authoritiesByUsernameQuery("SELECT username, role FROM roles " +
                    "WHERE username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
