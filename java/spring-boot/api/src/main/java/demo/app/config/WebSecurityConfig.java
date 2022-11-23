package demo.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration applied on all web endpoints defined for this
 * application. Any configuration on specific resources is applied
 * in addition to these global rules.
 */
@Configuration
@RequiredArgsConstructor
class WebSecurityConfig {

    /**
     * Configures basic security handler per HTTP session.
     * <p>
     * <ul>
     * <li>Stateless session (no session kept server-side)</li>
     * <li>CORS set up</li>
     * <li>Require the role "ACCESS" for all api paths</li>
     * <li>JWT converted into Spring token</li>
     * </ul>
     *
     * @param http security configuration
     * @throws Exception any error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.sessionManagement(smc -> {
            smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        http.authorizeRequests(arc -> {
            // declarative route configuration
            // .mvcMatchers("/api").hasAuthority("ROLE_ACCESS")
            arc.mvcMatchers("/api/**").authenticated();
            // add additional routes
            arc.anyRequest().authenticated(); //
        });
        http.oauth2ResourceServer().opaqueToken();

        return http.build();
    }

}