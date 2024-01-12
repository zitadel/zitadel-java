package demo.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
class UiController {

    @Qualifier("zitadel")
    private final RestTemplate restTemplate;

    @GetMapping("/")
    public String showIndex(Model model, Authentication auth, @RequestParam(required = false) boolean load) {
        var roleNames = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        model.addAttribute("roleNames", roleNames);
        var userInfo = ((DefaultOidcUser) auth.getPrincipal()).getUserInfo();
        model.addAttribute("userInfo", userInfo);

        if (load) {
            this.listTasks(model);
        }

        return "index";
    }

    @PostMapping("/add")
    public String addTask(Model model, Authentication auth, @RequestParam(required = true) String task) {
        log.debug("Calling backend API: begin");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(task, headers);

        restTemplate.postForLocation("http://localhost:18090/api/tasks", httpEntity);
        log.debug("Calling backend API: end");

        return "redirect:/?load=true";
    }

    private void listTasks(Model model) {
        log.debug("Calling backend API: begin");
        var payload = restTemplate.getForObject("http://localhost:18090/api/tasks", Object.class);
        log.debug("Calling backend API: end");
        model.addAttribute("tasks", payload);
    }
}
