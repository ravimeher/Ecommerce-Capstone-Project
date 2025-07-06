package org.example.userauthenticationservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PasswordResetToken extends BaseModel {

    @Column(unique = true)
    private String token;

    @OneToOne
    private User user;

    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, User user, LocalDateTime expiry) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiry;
    }

    public PasswordResetToken() {

    }
}