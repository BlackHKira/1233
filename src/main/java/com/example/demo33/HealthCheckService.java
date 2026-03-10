package com.example.demo33;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@Slf4j
public class HealthCheckService {

    @Autowired private GuestRepository guestRepository; // Postgres
    @Autowired private StringRedisTemplate redisTemplate; // Redis
    @Autowired private KafkaTemplate<String, String> kafkaTemplate; // Kafka

    // Nếu bạn chưa cài thư viện Elasticsearch thì có thể comment/xóa tạm dòng dưới
    // @Autowired private ElasticsearchOperations elasticsearchOperations;

    public String testFullSystem() {
        try {
            String testId = "test-" + System.currentTimeMillis();
            Guest testGuest = new Guest(testId, "Khải DevOps", "khai@viettel.com");

            // 1. Test Postgres
            guestRepository.save(testGuest);
            log.info("Postgres: OK");

            // 2. Test Redis
            redisTemplate.opsForValue().set("guest:" + testId, "Active", Duration.ofMinutes(5));
            log.info("Redis: OK");

            // 3. Test Kafka
            kafkaTemplate.send("guest-topic", "New guest registered: " + testId);
            log.info("Kafka: OK");

            return "Hệ thống hoạt động hoàn hảo!";
        } catch (Exception e) {
            log.error("Lỗi kết nối: " + e.getMessage());
            return "Thất bại: " + e.getMessage();
        }
    }
}