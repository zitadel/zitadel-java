package demo.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

import demo.app.support.zitadel.CustomAuthorityOpaqueTokenIntrospector;

/**
 * Configuration applied on all web endpoints defined for this
 * application. Any configuration on specific resources is applied
 * in addition to these global rules.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
class WebSecurityConfig {

    @Autowired
    private OpaqueTokenIntrospector introspector;

    /**
     * Configures basic security handler per HTTP session.
     * <p>
     * <ul>
     * <li>Stateless session (no session kept server-side)</li>
     * <li>CORS set up</li>
     * <li>Require authentication for the /api/tasks paths</li>
     * <li>access token converted into Spring token</li>
     * </ul>
     *
     * @param http security configuration
     * @throws Exception any error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(c -> c.disable())
                .sessionManagement(smc -> {
                    smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(arc -> {
                    arc.requestMatchers("/api/tasks").authenticated();
                    arc.anyRequest().permitAll();
                })
                .oauth2ResourceServer(rs -> rs.opaqueToken(a -> a.introspector(this.introspector())));

        return http.build();
    }

    private OpaqueTokenIntrospector introspector() {
        return new CustomAuthorityOpaqueTokenIntrospector(introspector);
    }

}