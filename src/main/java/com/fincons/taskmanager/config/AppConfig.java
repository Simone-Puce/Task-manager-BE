package com.fincons.taskmanager.config;

import com.fincons.taskmanager.security.SpringSecurityAuditorAwareImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAwareImpl();
    }
    @Bean
    public ModelMapper modelMapperStandard(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }

}
