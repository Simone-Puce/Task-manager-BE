package com.fincons.taskmanager.utility;


import com.fincons.taskmanager.enums.RoleEndpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {

    public String path;
    
    public List<RoleEndpoint> roles;
}
