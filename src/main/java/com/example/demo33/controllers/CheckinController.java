package com.example.demo33.controllers;

import com.example.demo33.GuestService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public  CheckinController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/{guestId}")
    public String checkinQrCode(@PathVariable("guestId") Long guestId){
        kafkaTemplate.send("checkin", guestId);
        return "success";
    }
}
