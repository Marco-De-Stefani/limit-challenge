package com.deste.apis;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WhiteController {

    @GetMapping("/white")
    public ResponseEntity<String> white() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Cost", "1");
        headers.add("X-API-Group", "api_white");
        return ResponseEntity.ok()
                .headers(headers)
                .body("White");
    }
}
