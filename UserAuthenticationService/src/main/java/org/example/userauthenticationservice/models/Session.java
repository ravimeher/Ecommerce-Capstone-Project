package org.example.userauthenticationservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session extends BaseModel {
    @ManyToOne
    private User user;
    private String token;
    @Enumerated(EnumType.STRING)
    private SessionState sessionState;

}

//Session    User

// M        1
// 1        1

