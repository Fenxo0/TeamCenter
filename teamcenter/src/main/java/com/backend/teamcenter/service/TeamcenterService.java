package com.backend.teamcenter.service;

import com.backend.teamcenter.model.ResponseModel;

import java.io.IOException;

public interface TeamcenterService {

    ResponseModel login(String username, String password);
    ResponseModel getSavedQueries(String session);

    ResponseModel executeSavedQueries(String session, String id, String name);
}
