package main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
    @Bean
    public UserDetailsService userDetailsService(){
    	return new CustomUserDetailsService();
    }
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
		AuthenticationManager manager = builder.build();
		
		http
		    .authorizeHttpRequests()
		    .requestMatchers("/login").permitAll()
		    .requestMatchers("/home").permitAll()
		    .requestMatchers("/student/**").hasAuthority("STUDENT")
		    .requestMatchers("/teacher/**").hasAuthority("TEACHER")
		    .anyRequest().authenticated()
		    .and()
		    .formLogin()
		    .loginPage("/login")
		    .loginProcessingUrl("/sign-in")
		    .defaultSuccessUrl("/home", true)
		    .failureUrl("/login?error=true")
		    .and()
		    .logout()
		    .deleteCookies("JSESSIONID")
		    .clearAuthentication(true)
		    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		    .logoutSuccessUrl("/login?logout")
		    .and()
		    .exceptionHandling()
		    .accessDeniedPage("/403")
		    .and()
		    .csrf().disable()
		    .authenticationManager(manager)	 
		    .httpBasic();	
		
		return http.build();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) ->
                web.ignoring()
                        .requestMatchers("/js/**", "/css/**");
    }


}
