package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.entity.Lane;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LaneMapper {

    @Autowired
    private ModelMapper modelMapperForLane;

    public LaneDTO mapToDTO(Lane lane) {
        return modelMapperForLane.map(lane, LaneDTO.class);
    }
    public List<LaneDTO> mapEntitiesToDTOs(List<Lane> lanes){
        return lanes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Lane mapToEntity(LaneDTO laneDTO){
        return modelMapperForLane.map(laneDTO, Lane.class);
    }

}
