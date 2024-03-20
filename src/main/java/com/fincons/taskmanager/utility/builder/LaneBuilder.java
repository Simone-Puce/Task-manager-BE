package com.fincons.taskmanager.utility.builder;

import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.entity.Lane;

import java.util.ArrayList;
import java.util.List;

public class LaneBuilder {

    public static List<Lane> getDefaultListLane(){
        List<Lane> lanes = new ArrayList<>();
        Lane lane = new Lane (null,"TO DO");
        lanes.add(lane);
        return lanes;
    }
    public static Lane getDefaultLane() {
        return new Lane (null,"TO DO");
    }
}
