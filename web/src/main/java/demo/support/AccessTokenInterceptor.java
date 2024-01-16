package demo.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenInterceptor implements ClientHttpRequestInterceptor {

    private final TokenAccessor tokenAccessor;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        var accessToken = tokenAccessor.getAccessTokenForCurrentUser();

        if (accessToken != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, accessToken.getTokenType().getValue()+ " " + accessToken.getTokenValue());
            log.debug("user access token set");
        }

        return execution.execute(request, body);
    }
}
