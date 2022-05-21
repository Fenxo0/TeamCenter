package com.backend.teamcenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ObjectModel {

    private Map<String, String> object = new HashMap<>();

    public void addAllObject(Map<String, String> objects) {
        this.object.putAll(objects);
    }
}
