package demo.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class ApiController {

    @Qualifier("zitadel")
    private final RestTemplate restTemplate;

    @GetMapping("/tasks")
    Object greetme(Authentication auth) {

        log.debug("Calling backend API: begin");
        var payload = restTemplate.getForObject("http://localhost:18090/api/tasks", Object.class);
        log.debug("Calling backend API: end");
        return payload;
    }

    @GetMapping("/list")
    Object list(Authentication auth) {

        log.debug("Calling backend API: begin");
        var payload = restTemplate.getForObject("http://localhost:18090/api/tasks", Object.class);
        log.debug("Calling backend API: end");
        return payload;
    }
}
