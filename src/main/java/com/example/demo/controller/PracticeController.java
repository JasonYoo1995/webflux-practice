package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PracticeController {
    @GetMapping("1")
    Mono<Void> practice1(){
        return null;
    }
}
