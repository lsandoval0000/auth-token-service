package org.abc.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@RegisterForReflection
public class TokenRequest {
    private String msIdentifier;
    private String msName;
    private String profile;
    private String token;
}
