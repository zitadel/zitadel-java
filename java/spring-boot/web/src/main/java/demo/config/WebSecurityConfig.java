package demo.config;

import demo.support.zitadel.ZitadelGrantedAuthoritiesMapper;
import demo.support.zitadel.ZitadelLogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
class WebSecurityConfig {

    private final ZitadelLogoutHandler zitadelLogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {

        http.authorizeRequests(arc -> {
            // declarative route configuration
            // add additional routes
            arc.antMatchers("/webjars/**", "/resources/**", "/css/**").permitAll();
            arc.anyRequest().fullyAuthenticated();
        });

        // by default spring security oauth2 client does not support PKCE for confidential clients for auth code grant flow,
        // we explicitly enable the PKCE customization here.
        http.oauth2Client(o2cc -> {
            var oauth2AuthRequestResolver = new DefaultOAuth2AuthorizationRequestResolver( //
                    clientRegistrationRepository, //
                    OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI //
            );
            // Note: back-ported the OAuth2AuthorizationRequestCustomizers from Spring Security 5.7,
            // replace with original version once Spring Boot support Spring Security 5.7.
            //oauth2AuthRequestResolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

            o2cc.authorizationCodeGrant().authorizationRequestResolver(oauth2AuthRequestResolver);
        });

        http.oauth2Login(o2lc -> {
            o2lc.userInfoEndpoint().userAuthoritiesMapper(userAuthoritiesMapper());
        });
        http.logout(lc -> {
            lc.addLogoutHandler(zitadelLogoutHandler);
        });

        return http.build();
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return new ZitadelGrantedAuthoritiesMapper();
    }
}