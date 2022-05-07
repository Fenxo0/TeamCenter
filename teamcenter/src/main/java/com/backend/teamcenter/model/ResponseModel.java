package com.backend.teamcenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpHeaders;

@Data
@AllArgsConstructor
@ToString
public class ResponseModel {

    private HttpHeaders headers;
    private String body;

}
