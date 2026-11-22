package com.copiloto.ia;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ia")
public class IaPruebaController {
    private final ChatClient chatClient;

    public IaPruebaController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ping")
    String ping(@RequestParam(defaultValue = "¿Qué es una base de datos vectorial? Responde en dos frases") String message) {
        return chatClient.prompt().user(message).call().content();
    }
}
