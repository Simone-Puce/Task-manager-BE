package com.fincons.taskmanager.config;

import com.fincons.taskmanager.dto.BoardDTO;
import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.security.SpringSecurityAuditorAwareImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
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
        modelMapper.addMappings(new PropertyMap<Lane, LaneDTO>() {
            @Override
            protected void configure(){
                skip(destination.getBoards());
            }
        });
        return modelMapper;
    }
    @Bean
    public ModelMapper modelMapperForLane(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Board, BoardDTO>() {
            @Override
            protected void configure(){
                skip(destination.getLanes());
                skip(destination.getTasks());
                skip(destination.getUsers());
            }
        });
        return modelMapper;
    }

}
