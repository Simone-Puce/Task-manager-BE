package com.fincons.taskmanager.utility.builder;

import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.entity.Lane;

import java.util.ArrayList;
import java.util.List;

public class LaneBuilder {

    public static List<Lane> getDefaultListLane(){
        List<Lane> lanes = new ArrayList<>();
        Lane toDo = new Lane (null,"TO DO");
        Lane inProgress = new Lane (null,"IN PROGRESS");
        Lane done = new Lane (null,"DONE");
        lanes.add(toDo);
        lanes.add(inProgress);
        lanes.add(done);
        return lanes;
    }
    public static Lane getDefaultLane() {
        return new Lane (null,"TO DO");
    }
}
