package com.backend.teamcenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ObjectModel {

    private Map<String, String> objects = new LinkedHashMap<>();
    private List<String> users = new ArrayList<>();
    private List<String> groups = new ArrayList<>();

    public void addAllObject(Map<String, String> objects) {
        this.objects.putAll(objects);
    }

    public void addAllUsers(List<String> users) {
        this.users.addAll(users);
    }

    public void addAllGroups(List<String> groups) {
        this.groups.addAll(groups);
    }
}
