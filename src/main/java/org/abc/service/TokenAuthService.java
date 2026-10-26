package org.abc.service;

import org.abc.dto.TokenRequest;

public interface TokenAuthService {
    boolean validateTokenForCreation(TokenRequest tokenRequest);
    boolean validateTokenForRefresh(TokenRequest tokenRequest);
}
