package com.fincons.taskmanager.security;

import com.fincons.taskmanager.enums.RoleEndpoint;
import com.fincons.taskmanager.jwt.JwtAuthenticationFilter;
import com.fincons.taskmanager.jwt.JwtUnauthorizedAuthenticationEntryPoint;
import com.fincons.taskmanager.utility.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Autowired
    private JwtUnauthorizedAuthenticationEntryPoint authenticationExceptionEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Value("${application.context}")
    private String appContext;
    @Value("${role.base.uri}")
    private String roleBaseUri;
    @Value("${modify.user}")
    private String modifyUser;
    @Value("${update.user.password}")
    private String updateUserPassword;
    @Value("${registered.users}")
    private String registeredUsers;
    @Value("${login.base.uri}")
    private String loginBaseUri;
    @Value("${error.base.uri}")
    private String errorBaseUri;
    @Value("${register.base.uri}")
    private String registerBaseUri;
    @Value("${delete.user-by-email}")
    private String deleteUserByEmail;
    @Value("${task.base.uri}")
    private String taskBaseUri;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        /*List<Endpoint> endpoints = Arrays.asList(
                new Endpoint(appContext + roleBaseUri + "/**", Arrays.asList(RoleEndpoint.ADMIN, RoleEndpoint.USER)),
                new Endpoint(appContext + registeredUsers, List.of(RoleEndpoint.ADMIN)),
                new Endpoint(appContext + deleteUserByEmail + "/**", List.of(RoleEndpoint.ADMIN))
        );
        http.authorizeHttpRequests(authz -> {
            for (Endpoint e: endpoints) {
                if (e.getRoles().contains(RoleEndpoint.ADMIN) && e.getRoles().contains(RoleEndpoint.USER)) {
                    authz.requestMatchers(HttpMethod.GET, e.getPath()).hasAnyRole("ADMIN","USER");
                    authz.requestMatchers(e.getPath()).hasRole("ADMIN");
                }else if(e.getRoles().contains(RoleEndpoint.ADMIN) && e.getRoles().size() == 1){
                    authz.requestMatchers(e.getPath()).hasRole("ADMIN");
                } else if (e.getRoles().contains(RoleEndpoint.USER) && e.getRoles().size() == 1) {
                    authz.requestMatchers(e.getPath()).hasRole("USER");
                }
            }
            authz.requestMatchers(appContext + loginBaseUri).permitAll()
                    .requestMatchers(appContext +registerBaseUri).permitAll()
                    .requestMatchers(appContext + errorBaseUri).permitAll()
                    .requestMatchers(appContext + modifyUser).authenticated()
                    .requestMatchers(appContext + updateUserPassword).authenticated()
                    .anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults());*/

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(appContext + loginBaseUri).permitAll()
                    .requestMatchers(appContext + registerBaseUri).permitAll()
                    .requestMatchers(appContext + taskBaseUri + "/**").hasAnyRole("USER","EDITOR","ADMIN");
        }).httpBasic(Customizer.withDefaults());

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationExceptionEntryPoint));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
