package org.stapledon.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${spring.application.name:none}")
    private String appName;

    @GetMapping("/")
    public ResponseEntity<String> getHome() {
        return ResponseEntity.ok(appName);
    }
}