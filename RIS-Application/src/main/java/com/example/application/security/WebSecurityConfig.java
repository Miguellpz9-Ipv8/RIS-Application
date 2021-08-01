package com.example.application.security;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
class LoginRedirectSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserDetailsService();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/css/**", "/images/**").permitAll()
            .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
            .antMatchers("/referral/**").hasAnyAuthority("ADMIN", "REFERRAL_DOCTOR")
            .antMatchers("/staff/**").hasAnyAuthority("ADMIN", "RECEPTIONIST")
            .antMatchers("/imaging/**").hasAnyAuthority("ADMIN", "TECHNICIAN")
            .antMatchers("/appointments").hasAnyAuthority("ADMIN", "RECEPTIONIST", "TECHNICIAN", "RADIOLOGIST")
            .antMatchers("/orders").hasAnyAuthority("ADMIN", "RECEPTIONIST", "RADIOLOGIST")
            .antMatchers("/order/**").hasAnyAuthority("ADMIN", "RECEPTIONIST", "RADIOLOGIST")
            .antMatchers("/home").hasAnyAuthority("ADMIN", "USER", "REFERRAL_DOCTOR",  "RECEPTIONIST", "TECHNICIAN", "RADIOLOGIST")
            .anyRequest().authenticated()
            
            .and().formLogin().loginPage("/login").loginProcessingUrl("/user_login")
            .failureUrl("/login?error=loginError").defaultSuccessUrl("/home").permitAll()
            
            .and().logout().logoutUrl("/user_logout").logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
            .and().exceptionHandling().accessDeniedPage("/403")
            .and().csrf().disable();
    }
}
