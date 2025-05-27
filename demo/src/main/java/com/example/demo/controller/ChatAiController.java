package com.example.demo.controller;


import com.example.demo.service.ChatAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class ChatAiController {
    private final ChatAiService chatAiService;

    public ChatAiController(ChatAiService chatAiService) {
        this.chatAiService = chatAiService;
    }

    @GetMapping("/ask")
    public ResponseEntity<String> ask(@RequestParam String question) {
        String answer = chatAiService.processQuestionToJson(question);
        return ResponseEntity.ok().body(answer); // Trả về trực tiếp String
    }

}
