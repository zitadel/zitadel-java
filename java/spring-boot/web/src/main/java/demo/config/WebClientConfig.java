package demo.config;

import demo.support.AccessTokenInterceptor;
import demo.support.TokenAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
class WebClientConfig {

    private final TokenAccessor tokenAccessor;

    @Bean
    @Qualifier("zitadel")
    RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .defaultHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("accept", MediaType.APPLICATION_JSON_VALUE)
                .interceptors(new AccessTokenInterceptor(tokenAccessor))
                .build();
    }
}
