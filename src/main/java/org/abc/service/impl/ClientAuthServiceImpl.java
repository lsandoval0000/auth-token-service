package org.abc.service.impl;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.abc.dto.TokenRequest;
import org.abc.dto.TokenResponse;
import org.abc.entity.GeneratedTokenHistory;
import org.abc.mapper.TokenObjectsMapper;
import org.abc.repository.GeneratedTokenHistoryRepository;
import org.abc.service.ClientAuthService;
import org.abc.service.TokenAuthService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.Set;

import static org.abc.util.Constants.ACTIVE_STATUS;
import static org.abc.util.Constants.TOKEN_EXPIRATION_TIME;

@RequiredArgsConstructor
@ApplicationScoped
public class ClientAuthServiceImpl implements ClientAuthService {
    private final TokenAuthService tokenAuthService;
    private final GeneratedTokenHistoryRepository generatedTokenHistoryRepository;
    private final TokenObjectsMapper tokenObjectsMapper;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    private String issuer;
    
    @Transactional
    @Override
    public TokenResponse generateToken(TokenRequest tokenRequest) {
        boolean validation = tokenAuthService.validateTokenForCreation(tokenRequest);
        if (!validation) {
            return new TokenResponse();
        }
        String jwt = generateJwt(tokenRequest);
        GeneratedTokenHistory entity = new GeneratedTokenHistory();
        entity.setToken(jwt);
        entity.setStatus(ACTIVE_STATUS.charAt(0));
        entity.setExpiresIn(TOKEN_EXPIRATION_TIME);
        generatedTokenHistoryRepository.persist(entity);
        return tokenObjectsMapper.entityToResponse(entity);
    }

    @Transactional
    @Override
    public TokenResponse refreshToken(TokenRequest tokenRequest) {
        boolean validation = tokenAuthService.validateTokenForRefresh(tokenRequest);
        if (!validation) {
            return new TokenResponse();
        }
        generatedTokenHistoryRepository.revokeToken(tokenRequest.getToken());
        String jwt = generateJwt(tokenRequest);
        GeneratedTokenHistory entity = new GeneratedTokenHistory();
        entity.setToken(jwt);
        entity.setStatus(ACTIVE_STATUS.charAt(0));
        entity.setExpiresIn(TOKEN_EXPIRATION_TIME);
        generatedTokenHistoryRepository.persist(entity);
        return tokenObjectsMapper.entityToResponse(entity);
    }

    @Transactional
    @Override
    public void revokeToken(String token) {
        generatedTokenHistoryRepository.revokeToken(token);
    }
    
    private String generateJwt(TokenRequest tokenRequest) {
        return Jwt.issuer(issuer)
                .subject(tokenRequest.getMsName())
                .groups(Set.of(tokenRequest.getProfile()))
                .expiresIn(Duration.ofSeconds(TOKEN_EXPIRATION_TIME))
                .sign();
    }
}
