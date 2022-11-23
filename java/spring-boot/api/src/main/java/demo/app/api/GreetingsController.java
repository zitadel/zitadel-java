package demo.app.api;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
class GreetingsController {

    @GetMapping("/api/greet/me")
    Object greetme(Authentication auth) {
        var tokenAttributes = ((BearerTokenAuthentication) auth).getTokenAttributes();
        var message = "Greetings my friend " + tokenAttributes.get(StandardClaimNames.PREFERRED_USERNAME) + " " + Instant.now();
        return Map.of("message", message);
    }
}
