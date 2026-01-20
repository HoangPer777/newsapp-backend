package com.example.newsapp.modules.article.service;

import org.springframework.beans.factory.annotation.Value; // Nhớ import cái này
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Spring sẽ lấy link từ application.properties dán vào đây
    @Value("${app.chatbot.sync-url}")
    private String pythonSyncUrl;

    @Async
    public void syncChatbotData() {
        try {
            System.out.println("Current Thread: " + Thread.currentThread().getName());

            // LOG ra để Han kiểm tra xem nó có lấy đúng địa chỉ localhost:8000 không
            System.out.println(">>> Đang gọi sang Python tại: " + pythonSyncUrl);

            // Dùng biến pythonSyncUrl thay cho cái String cũ
            restTemplate.postForEntity(pythonSyncUrl, null, String.class);

            System.out.println(">>> Đã gửi tín hiệu Sync thành công!");
        } catch (Exception e) {
            System.err.println(">>> Lỗi kết nối Chatbot: " + e.getMessage());
            // e.printStackTrace(); // Có thể bật lại nếu muốn xem lỗi chi tiết
        }
    }
}