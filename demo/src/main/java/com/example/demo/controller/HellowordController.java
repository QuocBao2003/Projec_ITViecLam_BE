package com.example.demo.controller;

import com.example.demo.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HellowordController {

    @GetMapping("/")

    public String helloword() throws IdInvalidException {

        return "Hello World";
    }
}
