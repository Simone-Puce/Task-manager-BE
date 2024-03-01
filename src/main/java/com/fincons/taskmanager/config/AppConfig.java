package com.fincons.taskmanager.config;

import com.fincons.taskmanager.dto.*;
import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.security.SpringSecurityAuditorAwareImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAwareImpl();
    }

    @Bean
    public ModelMapper modelMapperStandard() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
        //modelMapper.getConfiguration().setAmbiguityIgnored(true);
        //modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    @Bean
    public ModelMapper modelMapperForTask() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Attachment, AttachmentDTO>() {
            @Override
            protected void configure() {
                skip(destination.getTaskCode());
            }
        });
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForBoard() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Task, TaskDTO>() {
            protected void configure() {
                skip(destination.getAttachments());
                skip(destination.getBoardCode());
                skip(destination.getCreatedBy());
                skip(destination.getModifiedBy());
                skip(destination.getCreatedDate());
                skip(destination.getModifiedDate());
                }
        }
        );
        modelMapper.addMappings(new PropertyMap<Lane, LaneDTO>() {
            @Override
            protected void configure() {
                skip(destination.getBoards());
            }
        });
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForLane() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Board, BoardDTO>() {
            @Override
            protected void configure() {
                skip(destination.getLanes());
                skip(destination.getTasks());
                skip(destination.getUsers());
            }
        });
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForUserBoard() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForUser() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Role, RoleDTO>() {
            @Override
            protected void configure() {
                skip(destination.getUsers());
            }
        });
        return modelMapper;
    }

    @Bean
    ModelMapper modelMapperForTaskUser() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

}
