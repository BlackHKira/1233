package com.example.demo33.controllers;

import com.example.demo33.Guest;
import com.example.demo33.GuestRepository;
import com.example.demo33.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;
    private final GuestRepository guestRepository;

    public GuestController(GuestService guestService, GuestRepository guestRepository) {
        this.guestService = guestService;
        this.guestRepository = guestRepository;
    }

    // 1. API ĐĂNG KÝ
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Guest guest) {
        return ResponseEntity.ok(guestService.registerGuest(guest));
    }

    // ================== CLASS HỨNG DỮ LIỆU JSON ==================
    public static class CheckinRequest {
        public String phone;
        public String name;
        public int accompanying = 0; // Mặc định đi 1 mình (0 người đi cùng)
    }
    // ==============================================================

    // 2. API CHECK-IN BẰNG TÊN (Nhận Raw JSON)
    @PostMapping("/checkin")
    public ResponseEntity<String> checkin(@RequestBody CheckinRequest request) {

        if (request.name == null || request.name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ LỖI: Tên khách mời không được để trống!");
        }

        // CHỈ TRUYỀN 3 BIẾN (Service sẽ tự tính giới hạn vé)
        String ketQuaCheckin = guestService.processCheckinByPhoneAndName(
                request.phone,
                request.name,
                request.accompanying
        );
        return ResponseEntity.ok(ketQuaCheckin);
    }

    // 3. API DASHBOARD (Lưu ý: Dùng phương thức GET)
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok(guestService.getEventDashboard());
    }

    // 4. API TEST SERVER SỐNG
    @GetMapping("/ping")
    public String ping() {
        return "Chạy tốt";

    }

    @DeleteMapping("/clear-all")
    public ResponseEntity<String> clearAll() {
        guestRepository.deleteAll();
        return ResponseEntity.ok("Đã xóa sạch dữ liệu khách mời!");
    }

}