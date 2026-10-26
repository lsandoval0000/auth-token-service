package org.abc.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.abc.dto.TokenRequest;
import org.abc.dto.TokenResponse;
import org.abc.service.ClientAuthService;

import java.util.Optional;

@Path("/auth")
@RequiredArgsConstructor
public class TokenController {
    
    private final ClientAuthService clientAuthService;
    
    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response generateToken(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = clientAuthService.generateToken(tokenRequest);
        if (Optional.ofNullable(tokenResponse.getToken()).isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(tokenResponse).build();
    }
    
    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response refreshToken(
            @HeaderParam("Authorization") String authorizationToken, 
            TokenRequest tokenRequest) {
        tokenRequest.setToken(authorizationToken);
        TokenResponse tokenResponse = clientAuthService.refreshToken(tokenRequest);
        return Response.ok(tokenResponse).build();
    }
    
    @POST
    @Path("/revoke")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response revokeToken(
            @HeaderParam("Authorization") String authorizationToken) {
        clientAuthService.revokeToken(authorizationToken);
        return Response.ok().build();
    }
}
