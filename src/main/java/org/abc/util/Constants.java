package org.abc.util;

import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Constants {
    public static final String ACTIVE_STATUS = "A";
    public static final String INACTIVE_STATUS = "I";
    public static final Map<String, String> registeredProfiles = Map.of(
            "exchange-rate-informer", "exchange-rate-query,exchange-rate-consumer"
    );
    public static final long TOKEN_EXPIRATION_TIME = 3600;
}
