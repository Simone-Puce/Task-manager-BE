package com.fincons.taskmanager.security;

import com.fincons.taskmanager.jwt.JwtAuthenticationFilter;
import com.fincons.taskmanager.jwt.JwtUnauthorizedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    @Value("${log4j.base.uri}")
    private String logBaseUri;
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
    @Value("${attachment.base.uri}")
    private String attachmentBaseUri;
    @Value("${board.base.uri}")
    private String boardBaseUri;
    @Value("${lane.base.uri}")
    private String laneBaseUri;
    @Value("${board.lane.base.uri}")
    private String boardLaneBaseUri;
    @Value("${user.board.base.uri}")
    private String userBoardBaseUri;
    @Value("${task.user.base.uri}")
    private String taskUserBaseUri;

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

        http.authorizeHttpRequests(auth -> {
            auth
                    //ADMIN
                    .requestMatchers(appContext + logBaseUri + "/**").hasAnyRole("ADMIN")
                    .requestMatchers(appContext + userBoardBaseUri + "/").hasAnyRole("ADMIN", "USER")
                    .requestMatchers(appContext + boardBaseUri + "/").hasAnyRole("ADMIN")
                    .requestMatchers(appContext + roleBaseUri + "/").hasAnyRole("ADMIN")

                    //EDITOR & USER
                    .requestMatchers(appContext + laneBaseUri + "/").hasAnyRole("USER")
                    .requestMatchers(appContext + taskUserBaseUri + "/").hasAnyRole("USER")
                    .requestMatchers(appContext + attachmentBaseUri + "/").hasAnyRole("USER")
                    .requestMatchers(appContext + taskBaseUri + "/").hasAnyRole("USER")

                    //General authorizations
                    .requestMatchers(appContext + loginBaseUri).permitAll()
                    .requestMatchers(appContext + registerBaseUri).permitAll()
                    .requestMatchers(appContext + errorBaseUri).permitAll()
                    .requestMatchers(appContext + modifyUser).authenticated()
                    .requestMatchers(appContext + updateUserPassword).authenticated()
                    .requestMatchers(appContext + deleteUserByEmail).hasAnyRole("ADMIN") //TO HANDLE? BUT I THINK ONLY ADMIN CAN DELETE IT
                    .anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults());

        //ADMIN -> CRUD SULLE BOARD    OK

        //EDITOR -> CRUD SUI TASK + ASSOCIAZIONE task a board e task a user

        //USER -> create + delete sui task
        //          -> update + delete sui task a lui assegnati
        //          -> cambio status sui task a lui assegnati

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationExceptionEntryPoint));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
