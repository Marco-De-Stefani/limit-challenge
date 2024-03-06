package com.deste.apis;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BlackController {

    @GetMapping("/black")
    public ResponseEntity<String> black() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Cost", "2");
        headers.add("X-API-Group", "api_black");
        return ResponseEntity.ok()
                .headers(headers)
                .body("Black");
    }
}
