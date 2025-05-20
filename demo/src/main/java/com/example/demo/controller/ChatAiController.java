package com.example.demo.controller;


import com.example.demo.service.ChatAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String, String>> ask(@RequestParam String question) {
        String answer = chatAiService.processQuestionToJson(question);
        return ResponseEntity.ok().body(Collections.singletonMap("", answer));
    }

}
