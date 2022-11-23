package demo.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAccessor {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuth2AccessToken getAccessTokenForCurrentUser() {
        return getAccessToken(SecurityContextHolder.getContext().getAuthentication());
    }

    public OAuth2AccessToken getAccessToken(Authentication auth) {

        log.debug("Get AccessToken for current user {}: begin", auth.getName());
        var authToken = (OAuth2AuthenticationToken) auth;
        var clientId = authToken.getAuthorizedClientRegistrationId();
        var username = auth.getName();
        var oauth2Client = authorizedClientService.loadAuthorizedClient(clientId, username);

        if (oauth2Client == null) {
            log.warn("Get AccessToken for current user failed: client not found");
            return null;
        }

        var accessToken = oauth2Client.getAccessToken();
        log.debug("Get AccessToken for current user {}: end", auth.getName());
        return accessToken;

    }
}
