package com.fincons.taskmanager.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class AppConfig {

    @Bean
    public ModelMapper modelMapperStandard(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }

}
