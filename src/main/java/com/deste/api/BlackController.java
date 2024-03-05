package com.deste.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BlackController {

    @GetMapping("/black")
    public ResponseEntity<String> white() {
        return ResponseEntity.ok()
                .body("Black");
    }
}
