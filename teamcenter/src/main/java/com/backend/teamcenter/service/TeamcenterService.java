package com.backend.teamcenter.service;

import com.backend.teamcenter.model.ResponseModel;
import com.teamcenter.soa.client.model.strong.Dataset;

import java.io.IOException;

public interface TeamcenterService {

    ResponseModel login(String username, String password);
    ResponseModel getSavedQueries(String session);

    ResponseModel executeSavedQueries(String session, String id, String name);

    public Dataset[] getDatasets(String itemId, String revisionId) throws Exception;
}
