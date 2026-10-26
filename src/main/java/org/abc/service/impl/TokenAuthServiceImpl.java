package org.abc.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.abc.config.ClientRegistry;
import org.abc.dto.TokenRequest;
import org.abc.entity.GeneratedTokenHistory;
import org.abc.repository.GeneratedTokenHistoryRepository;
import org.abc.service.TokenAuthService;

import java.util.Arrays;
import java.util.Optional;

import static org.abc.util.Constants.registeredProfiles;

@ApplicationScoped
@RequiredArgsConstructor
public class TokenAuthServiceImpl implements TokenAuthService {
    private final GeneratedTokenHistoryRepository generatedTokenHistoryRepository;
    private final ClientRegistry clientRegistry;

    @Override
    public boolean validateTokenForCreation(TokenRequest tokenRequest) {
        return clientRegistry.isRegistered(tokenRequest.getMsName()) && validateProfile(tokenRequest);
    }

    @Override
    public boolean validateTokenForRefresh(TokenRequest tokenRequest) {
        boolean isSecretValid = clientRegistry.isRegistered(tokenRequest.getMsName());
        Optional<GeneratedTokenHistory> byToken = generatedTokenHistoryRepository.findByToken(tokenRequest.getToken());
        return isSecretValid && byToken.isPresent() && validateProfile(tokenRequest);
    }

    private boolean validateProfile(TokenRequest tokenRequest) {
        boolean validProfile = registeredProfiles.containsKey(tokenRequest.getMsName());
        if (!validProfile) {
            return false;
        }
        String[] profiles = registeredProfiles.get(tokenRequest.getMsName()).split(",");
        return Arrays.stream(profiles).anyMatch(tokenRequest.getProfile()::equals);
    }
}
