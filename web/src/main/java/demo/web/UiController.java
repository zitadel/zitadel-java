package demo.web;

import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
class UiController {

    @Qualifier("zitadel")
    private final RestTemplate restTemplate;

    @GetMapping("/")
    public String showIndex(Model model, Authentication auth) {
        var roleNames = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        model.addAttribute("roleNames", roleNames);
        var userInfo = ((DefaultOidcUser) auth.getPrincipal()).getUserInfo();
        model.addAttribute("userInfo", userInfo);

        return "index";
    }

    @GetMapping("/tasks")
    public String showTasks(Model model, Authentication auth, @RequestParam(required = false) boolean load) {
        if (load) {
            this.listTasks(model);
        }

        return "tasks";
    }

    @PostMapping("/add")
    public String addTask(Model model, Authentication auth, @RequestParam(required = true) String task) {
        log.debug("Calling backend API: begin");
        restTemplate.postForLocation("http://localhost:18090/api/tasks", task);
        log.debug("Calling backend API: end");

        return "redirect:/tasks?load=true";
    }

    private void listTasks(Model model) {
        log.debug("Calling backend API: begin");
        var payload = restTemplate.getForObject("http://localhost:18090/api/tasks", Object.class);
        log.debug("Calling backend API: end");
        model.addAttribute("tasks", payload);
    }
}
