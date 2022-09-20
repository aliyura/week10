package com.decagon.oxygen.controllers;


import com.decagon.oxygen.pojos.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/api/ping")
    public APIResponse<String> ping(){
        return new APIResponse<>("Service is up and running",true, null);
    }
}
