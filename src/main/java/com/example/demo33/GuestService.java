package com.example.demo33;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class GuestService {

    private final GuestRepository postgresRepo;
    private final GuestSearchRepository elasticRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public GuestService(GuestRepository postgresRepo, GuestSearchRepository elasticRepo,
                        RedisTemplate<String, String> redisTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.postgresRepo = postgresRepo;
        this.elasticRepo = elasticRepo;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public String registerGuest(Guest guest) {
        // Gọi getPhone() và existsByPhone()
        if (guest.getPhone() == null || guest.getPhone().trim().isEmpty()) {
            return "❌ LỖI: Vui lòng nhập số điện thoại!";
        }
        if (postgresRepo.existsByPhone(guest.getPhone())) {
            return "⚠️ Số điện thoại đã tồn tại!";
        }
        guest.setCheckinCount(0);
        guest.setCheckedIn(false);
        if (guest.getTicketType() == null) guest.setTicketType("NORMAL");

        Guest savedGuest = postgresRepo.save(guest);
        elasticRepo.save(new GuestDocument(savedGuest));

        return "✅ ĐĂNG KÝ THÀNH CÔNG: Vé " + savedGuest.getTicketType() + " số " + savedGuest.getId();
    }

    @Transactional
    public String processCheckinByPhoneAndName(String phone, String name, int soNguoiDiCung) {
        // Gọi findByPhoneAndName()
        var guestOpt = postgresRepo.findByPhoneAndName(phone, name);

        if (guestOpt.isPresent()) {
            Guest guest = guestOpt.get();
            int gioiHanCuaVe = "VIP".equalsIgnoreCase(guest.getTicketType()) ? 5 : 1;
            int tongNguoiVao = 1 + soNguoiDiCung;

            if (guest.getCheckinCount() + tongNguoiVao <= gioiHanCuaVe) {
                guest.setCheckinCount(guest.getCheckinCount() + tongNguoiVao);
                guest.setCheckedIn(true);
                guest.setCheckInDate(LocalDate.now());

                postgresRepo.save(guest);
                elasticRepo.save(new GuestDocument(guest));
                redisTemplate.delete("guest_cache:" + guest.getId());

                return "✅ " + guest.getTicketType() + " CHECK-IN THÀNH CÔNG: (Tổng: " + guest.getCheckinCount() + "/" + gioiHanCuaVe + ").";
            }
            return "❌ TỪ CHỐI: Hạng vé " + guest.getTicketType() + " chỉ cho tối đa " + gioiHanCuaVe + " người!";
        }
        return "❌ LỖI: Thông tin không khớp!";
    }

    @Transactional
    public String processCheckin(Long id, int soNguoiDiCung) {
        var guestOpt = postgresRepo.findById(id);

        if (guestOpt.isPresent()) {
            Guest guest = guestOpt.get();
            int gioiHanCuaVe = "VIP".equalsIgnoreCase(guest.getTicketType()) ? 5 : 1;
            int tongNguoiVao = 1 + soNguoiDiCung;

            if (guest.getCheckinCount() + tongNguoiVao <= gioiHanCuaVe) {
                guest.setCheckinCount(guest.getCheckinCount() + tongNguoiVao);
                guest.setCheckedIn(true);
                guest.setCheckInDate(LocalDate.now());

                postgresRepo.save(guest);
                elasticRepo.save(new GuestDocument(guest));
                redisTemplate.delete("guest_cache:" + guest.getId());

                System.out.println("✅ [KAFKA] ĐÃ CHECK-IN THÀNH CÔNG CHO KHÁCH ID: " + id);
                return "✅ Thành công";
            }
            System.out.println("❌ [KAFKA] TỪ CHỐI ID " + id + ": Vượt quá số lượng vé!");
            return "❌ Vượt quá giới hạn vé";
        }
        System.out.println("❌ [KAFKA] LỖI: Không tìm thấy ID " + id);
        return "❌ Không tìm thấy khách";
    }

    public String getEventDashboard() {
        long countVip = postgresRepo.countByTicketTypeIgnoreCaseAndCheckedInTrue("VIP");
        long countNormal = postgresRepo.countByTicketTypeIgnoreCaseAndCheckedInTrue("Normal");
        Integer totalPeople = postgresRepo.sumTotalPeople();
        int finalTotal = (totalPeople != null) ? totalPeople : 0;

        return "📊 DASHBOARD SỰ KIỆN TRỰC TUYẾN:\n--------------------------------\n🔹 Tổng số người thực tế: " + finalTotal + "\n🔹 Số vé VIP đã check-in: " + countVip + "\n🔹 Số vé Thường đã check-in: " + countNormal + "\n--------------------------------";
    }

    @Transactional
    public void deleteAllGuests() {
        postgresRepo.deleteAll(); // Xóa sạch trong Database
        elasticRepo.deleteAll();  // Xóa sạch trong Elasticsearch (nếu có)
        System.out.println("♻️ Database đã được quét sạch!");
    }
}