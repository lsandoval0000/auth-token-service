package org.abc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "generated_token_history", schema = "abc_bank")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedTokenHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "expires_in")
    private Long expiresIn;
    private char status;
}
