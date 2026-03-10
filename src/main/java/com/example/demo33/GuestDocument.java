package com.example.demo33;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Repository;

@Document(indexName = "guests")
public class GuestDocument {

    @Id
    private String id; // ID trong Elasticsearch thường dùng kiểu String
    private String name;
    private String email;
    private boolean checkedIn;
    private int checkinCount;

    public GuestDocument() {}

    // Hàm tiện ích để chuyển đổi từ dữ liệu Postgres (Guest) sang Elasticsearch (GuestDocument)
    public GuestDocument(Guest guest) {
        this.id = guest.getId() != null ? guest.getId().toString() : null;
        this.name = guest.getName();
        this.email = guest.getEmail();
        this.checkedIn = guest.isCheckedIn();

    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }
    public int getCheckinCount() { return checkinCount; }
    public void setCheckinCount(int checkinCount) { this.checkinCount = checkinCount; }
}