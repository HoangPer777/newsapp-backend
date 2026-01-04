package com.example.newsapp.modules.article.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotService {
    private final RestTemplate restTemplate = new RestTemplate();
    // Dùng localhost nhé!
    private final String PYTHON_SYNC_URL = "http://localhost:8000/sync";

    @Async // <--- Quan trọng
    public void syncChatbotData() {
        try {
            // In log task name để kiểm tra
            System.out.println("Current Thread: " + Thread.currentThread().getName());
            restTemplate.postForEntity(PYTHON_SYNC_URL, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
