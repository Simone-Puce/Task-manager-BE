package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.entity.Lane;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LaneMapper {

    @Autowired
    private ModelMapper modelMapperStandard;

    public LaneDTO mapToDTO(Lane lane) {
        return modelMapperStandard.map(lane, LaneDTO.class);
    }

    public Lane mapToEntity(LaneDTO laneDTO){
        return modelMapperStandard.map(laneDTO, Lane.class);
    }

}
