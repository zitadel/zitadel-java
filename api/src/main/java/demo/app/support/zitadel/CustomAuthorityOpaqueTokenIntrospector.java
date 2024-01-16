package demo.app.support.zitadel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

public class CustomAuthorityOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    public static final String ZITADEL_ROLES_CLAIM = "urn:zitadel:iam:org:project:roles";

    public CustomAuthorityOpaqueTokenIntrospector(OpaqueTokenIntrospector delegate) {
        this.delegate = delegate;
    }

    private OpaqueTokenIntrospector delegate;

    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(), principal.getAttributes(), extractAuthorities(principal));
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        var authorities = new HashSet<GrantedAuthority>();

        HashMap<String, Object> claims = principal.getAttribute(ZITADEL_ROLES_CLAIM);
        if (claims == null) {
            return authorities;
        }

        claims.keySet().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        });

        return authorities;
    }
}
