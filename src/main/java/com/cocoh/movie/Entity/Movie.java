package com.cocoh.movie.Entity;

/* 2025-08-20 Movie 엔티티 구현 - 최현우 - */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "영화 제목", example = "Inception")
    private String movie_name; // 영화 제목

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    @Schema(description = "영화 개봉일", example = "2010-07-16")
    private LocalDate movie_date; // 영화 개봉일

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(nullable = false)
    @Schema(description = "상영 시간", example = "02:28:00")
    private LocalTime movie_time; // 상영 시간

    @Column(nullable = false)
    @Schema(description = "감독명", example = "Christopher Nolan")
    private String movie_director; // 감독명

    @Column
    @ColumnDefault("0")
    @Schema(description = "리뷰 수", example = "1240")
    private Integer movie_review_count; // 리뷰 수

    @Column(nullable = false)
    @Schema(description = "출연진 리스트", example = "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page")
    private String movie_cast_list; // 출연진 리스트

    @Column
    @ColumnDefault("0.0")
    @Schema(description = "평점", example = "0.0")
    private double movie_rating; // 평점

    @Column(nullable = false)
    @Schema(description = "장르", example = "로맨스, 코미디")
    private String movie_genre;

    @Column(nullable = false)
    @Schema(description = "영화 설명", example = "이 영화는 옛날 옛적 부터...")
    private String movie_description;

    @Column(nullable = true)
    private String movie_image;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt; // 데이터 삭제 시간

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    @JsonManagedReference
    private List<Review> reviews;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // softDelete 클래스 구현
    public void softDelete() {
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }



}
