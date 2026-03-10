package com.example.demo33;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional; // Nhớ import thư viện này

public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findAllByOrderByIdAsc();

    @Query("SELECT SUM(g.checkinCount) FROM Guest g")
    Integer sumTotalPeople();

    // THÊM DÒNG NÀY: Dạy Spring Boot tự động viết lệnh SQL tìm theo tên
    Optional<Guest> findByPhoneAndName(String phone, String name);
    boolean existsByPhone(String phone);
    long countByTicketTypeIgnoreCaseAndCheckedInTrue(String ticketType);

    void deleteAll();
}