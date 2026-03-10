package com.example.demo33.controllers; // 1. Khai báo đúng thư mục con chứa file này

import com.example.demo33.HealthCheckService; // 2. Chỉ đường (import) để gọi file Service ở thư mục ngoài vào
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @Autowired
    private HealthCheckService healthCheckService;

    // Tạo một API dùng phương thức GET
    @GetMapping("/health-check")
    public String triggerHealthCheck() {
        // Gọi sang tầng Service để chạy kịch bản test
        return healthCheckService.testFullSystem();
    }
}