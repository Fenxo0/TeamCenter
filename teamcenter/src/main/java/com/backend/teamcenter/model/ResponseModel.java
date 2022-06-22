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
    private ObjectModel objectModel;

    private UserTC user;

    public ResponseModel(HttpHeaders headers, UserTC user) {
        this.headers = headers;
        this.user = user;
    }

    public ResponseModel(HttpHeaders headers,
                         ObjectModel objectModel) {
        this.headers = headers;
        this.objectModel = objectModel;
    }

    public ResponseModel(HttpHeaders headers, String body) {
        this.headers = headers;
        this.body = body;
    }
}
