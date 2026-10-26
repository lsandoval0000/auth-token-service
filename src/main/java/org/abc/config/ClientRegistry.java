package org.abc.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

/**
 * Registry of API clients allowed to request tokens.
 * Client names are fixed in code; secrets are supplied through configuration
 * ({@code app.clients.<client>.secret}) so no credential is committed.
 */
@ApplicationScoped
public class ClientRegistry {
    static final String EXCHANGE_RATE_INFORMER = "exchange-rate-informer";

    @ConfigProperty(name = "app.clients.exchange-rate-informer.secret")
    String exchangeRateInformerSecret;

    public Map<String, String> registeredClients() {
        return Map.of(EXCHANGE_RATE_INFORMER, exchangeRateInformerSecret);
    }

    public boolean isRegistered(String clientName) {
        return registeredClients().containsKey(clientName);
    }
}
