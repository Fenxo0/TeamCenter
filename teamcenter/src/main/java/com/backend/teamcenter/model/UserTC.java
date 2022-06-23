package com.backend.teamcenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserTC {

    private String name;
    private String group;
    private String homeFolder;
    private String project;
    private String id;
    private String licenseLevel;
}
