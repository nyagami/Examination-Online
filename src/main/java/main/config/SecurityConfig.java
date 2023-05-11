package main.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Set;

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
					.requestMatchers("/register").permitAll()
					.requestMatchers("/process-register*").permitAll()
					.requestMatchers("/student/**").hasAuthority("STUDENT")
					.requestMatchers("/teacher/**").hasAuthority("TEACHER")
					.anyRequest().authenticated()
				.and()
					.formLogin()
						.loginPage("/login")
						.loginProcessingUrl("/sign-in")
						.successHandler(authenticationSuccessHandler())
						.failureUrl("/login?error=true")
				.and()
					.rememberMe()
						.rememberMeParameter("remember-me")
				.and()
					.logout()
						.deleteCookies("JSESSIONID")
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout=success")
				.and()
					.exceptionHandling()
						.accessDeniedPage("/error")
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

	private AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
												Authentication authentication) throws IOException, ServletException {
				Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
				if(authorities.contains("STUDENT")) {
					response.sendRedirect("/student/home");
				}  else if (authorities.contains("TEACHER")) {
					response.sendRedirect("/teacher/home");
				} else {
					throw new IllegalStateException("No authority found");
				}
			}
		};

	}

}
