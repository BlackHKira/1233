package com.example.demo33;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.example.demo33")
@RestController // THÊM DÒNG NÀY VÀO ĐÂY
public class Demo33Application {

    public static void main(String[] args) {
        // Thêm dòng in ra màn hình này:
        System.out.println("========== ĐÂY LÀ BẢN CODE MỚI NHẤT ĐÃ ĐƯỢC CẬP NHẬT ==========");

        SpringApplication.run(Demo33Application.class, args);
    }
}