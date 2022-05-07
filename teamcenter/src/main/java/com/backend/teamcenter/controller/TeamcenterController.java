package com.backend.teamcenter.controller;

import com.backend.teamcenter.model.RequestModel;
import com.backend.teamcenter.service.TeamcenterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TeamcenterController {

    private final TeamcenterService teamcenterService;

    public TeamcenterController(TeamcenterService teamcenterService) {
        this.teamcenterService = teamcenterService;
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody RequestModel request) {
        var response = teamcenterService.login(request.getUsername(), request.getPassword());
        //System.out.println(response);
        return new ResponseEntity<>(response.getBody(), response.getHeaders(), HttpStatus.OK);
    }

    @GetMapping("/getSavedQueries")
    ResponseEntity<?> getSavedQueries(@RequestHeader("Session") String session) {
        return null;
    }
}
