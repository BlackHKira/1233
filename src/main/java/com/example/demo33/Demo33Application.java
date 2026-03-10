package com.example.demo33;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone; // Nhớ import cái này

@SpringBootApplication
public class Demo33Application {

    public static void main(String[] args) {
        // Thêm dòng này để ép Java dùng múi giờ chuẩn trước khi gọi Database
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        SpringApplication.run(Demo33Application.class, args);
    }
}