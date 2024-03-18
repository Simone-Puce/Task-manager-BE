package com.fincons.taskmanager.config;

import com.fincons.taskmanager.dto.*;
import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.security.SpringSecurityAuditorAwareImpl;
import org.modelmapper.*;
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
        return new ModelMapper();
    }

    @Bean
    public ModelMapper modelMapperForTask() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<TaskUser, UserDTO> userBoardMapping = modelMapper.createTypeMap(TaskUser.class, UserDTO.class);
        userBoardMapping.addMappings(mapper -> {
            mapper.map(src -> src.getUser().getFirstName(), UserDTO::setFirstName);
            mapper.map(src -> src.getUser().getLastName(), UserDTO::setLastName);
            mapper.map(src -> src.getUser().getEmail(), UserDTO::setEmail);
        });
        modelMapper.addMappings(new PropertyMap<Attachment, AttachmentDTO>() {
            @Override
            protected void configure() {
                skip(destination.getTaskId());
            }
        });
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForBoard() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<UserBoard, UserDTO> userBoardMapping = modelMapper.createTypeMap(UserBoard.class, UserDTO.class);
        userBoardMapping.addMappings(mapper -> {
            mapper.map(src -> src.getUser().getFirstName(), UserDTO::setFirstName);
            mapper.map(src -> src.getUser().getLastName(), UserDTO::setLastName);
            mapper.map(src -> src.getUser().getEmail(), UserDTO::setEmail);
            mapper.map(UserBoard::getRoleCode, UserDTO::setRoleCodeForBoard);
        });

        modelMapper.addMappings(
                new PropertyMap<Lane, LaneDTO>() {
                    protected void configure() {
                        skip(destination.getBoardId());
                    }
                }
        );
        modelMapper.addMappings(
                new PropertyMap<Task, TaskDTO>() {
                    protected void configure() {
                        skip(destination.getAttachments());
                        skip(destination.getDescription());
                        skip(destination.getModifiedBy());
                        skip(destination.getCreatedBy());
                        skip(destination.getModifiedDate());
                        skip(destination.getCreatedDate());
                        skip(destination.getUsers());
                        //Gestire User nella configurazione?
                    }
                }
        );
        return modelMapper;

    }

    @Bean
    public ModelMapper modelMapperForLane() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Task, TaskDTO>() {
            @Override
            protected void configure() {
                skip(destination.getLaneId());
                skip(destination.getUsers());
                skip(destination.getAttachments());
                skip(destination.getModifiedBy());
                skip(destination.getCreatedBy());
                skip(destination.getModifiedDate());
                skip(destination.getCreatedDate());
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

        TypeMap<UserBoard, BoardDTO> userBoardMapping = modelMapper.createTypeMap(UserBoard.class, BoardDTO.class);
        userBoardMapping.addMappings(mapper -> {
            mapper.map(src -> src.getBoard().getBoardId(), BoardDTO::setBoardId);
            mapper.map(src -> src.getBoard().getBoardName(), BoardDTO::setBoardName);
            mapper.map(src -> src.getBoard().isActive(), BoardDTO::setActive);
        });
        TypeMap<TaskUser, TaskDTO> taskUserMapping = modelMapper.createTypeMap(TaskUser.class, TaskDTO.class);
        taskUserMapping.addMappings(mapper -> {
            mapper.map(src -> src.getTask().getTaskId(), TaskDTO::setTaskId);
            mapper.map(src -> src.getTask().getTaskName(), TaskDTO::setTaskName);
            mapper.map(src -> src.getTask().isActive(), TaskDTO::setActive);
        });

        modelMapper.addMappings(new PropertyMap<Role, RoleDTO>() {
            @Override
            protected void configure() {
                skip(destination.getUsers());
            }
        });
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForTaskUser() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForBoardLane() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
