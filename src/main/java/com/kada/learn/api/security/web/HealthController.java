package com.kada.learn.api.security.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<?> getMovie() {
        return ResponseEntity.ok().body("Greeting from health controller");
    }
}
