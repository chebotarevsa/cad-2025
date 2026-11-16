package ru.raspisa.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.raspisa.dto.PushSubscriptionRequest;
import ru.raspisa.service.PushService;

import java.util.Map;

@RestController
@RequestMapping("/api/push")
public class PushController {

    private final PushService pushService;

    public PushController(PushService pushService) {
        this.pushService = pushService;
    }

    @GetMapping("/public-key")
    public Map<String, Object> publicKey() {
        return Map.of(
                "enabled", pushService.isEnabled(),
                "publicKey", pushService.getPublicKey()
        );
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(@Valid @RequestBody PushSubscriptionRequest request) {
        pushService.subscribe(request);
    }
}
