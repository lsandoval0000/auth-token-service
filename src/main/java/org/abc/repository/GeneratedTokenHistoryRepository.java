package org.abc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.abc.entity.GeneratedTokenHistory;

import java.util.Map;
import java.util.Optional;

import static org.abc.util.Constants.ACTIVE_STATUS;
import static org.abc.util.Constants.INACTIVE_STATUS;

@ApplicationScoped
public class GeneratedTokenHistoryRepository implements PanacheRepository<GeneratedTokenHistory> {
    public void revokeToken(String token) {
        update("status = :disabled where token = :token", Map.of(
                "disabled", INACTIVE_STATUS,
                "token", token
        ));
    }
    
    public Optional<GeneratedTokenHistory> findByToken(String token) {
        return find("token = :token and status = :status", Map.of(
                "token", token,
                "status", ACTIVE_STATUS
        )).firstResultOptional();
    }
}
