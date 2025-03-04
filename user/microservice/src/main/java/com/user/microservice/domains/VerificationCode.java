package com.user.microservice.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int code;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationCode(User user) {
        this.user = user;
        this.code = generateVerificationCode();
        this.generatedAt = LocalDateTime.now();
        this.expiresAt = generatedAt.plusMinutes(15);
    }

    public int generateVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

}
