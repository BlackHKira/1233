package com.example.demo33.CheckIn;

import com.example.demo33.GuestService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaCheckinConsumer {

    private final GuestService guestService;

    public KafkaCheckinConsumer(GuestService guestService) {
        this.guestService = guestService;
    }

    @KafkaListener(topics = "guest_checkin_topic", groupId = "checkin_group")
    public void listenCheckin(String guestId) {
        try {
            Long id = Long.parseLong(guestId);
            guestService.processCheckin(id, 1); // Gọi sang Service
        } catch (NumberFormatException e) {
            System.err.println("Lỗi: ID khách mời gửi từ Kafka không đúng định dạng số: " + guestId);
        }
    }
}