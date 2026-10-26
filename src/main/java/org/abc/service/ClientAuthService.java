package org.abc.service;

import org.abc.dto.TokenRequest;
import org.abc.dto.TokenResponse;

public interface ClientAuthService {
    TokenResponse generateToken(TokenRequest tokenRequest);
    TokenResponse refreshToken(TokenRequest tokenRequest);
    void revokeToken(String token);
}
