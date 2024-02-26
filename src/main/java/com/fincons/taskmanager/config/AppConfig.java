package com.fincons.taskmanager.config;

import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.security.SpringSecurityAuditorAwareImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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
    @Bean
    public ModelMapper modelMapperForBoard(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Task, TaskDTO>() {
            @Override
            protected void configure(){
                skip(destination.getAttachments());
            }
        });

        return modelMapper;
    }

}
