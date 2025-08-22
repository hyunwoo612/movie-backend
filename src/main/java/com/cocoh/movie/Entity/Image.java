//package com.cocoh.movie.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.sql.Timestamp;
//import java.time.LocalDate;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//public class Image {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String uniqueName;
//
//    @Column(nullable = false)
//    private String originName;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "movie_id", nullable = false)
//    private Movie movie;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private Timestamp createdAt;
//
//    @PrePersist
//    public void onCreate() {
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//        this.createdAt = now;
//    }
//}
