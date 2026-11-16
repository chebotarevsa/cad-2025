package ru.raspisa.service;

import jakarta.annotation.PostConstruct;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.Subscription;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.dto.PushSubscriptionRequest;
import ru.raspisa.entity.PushSubscription;
import ru.raspisa.repository.PushSubscriptionRepository;

import java.security.Security;

@Service
public class PushService {

    private static final Logger log = LoggerFactory.getLogger(PushService.class);

    private final PushSubscriptionRepository repo;

    @Value("${push.vapid.public-key:}")
    private String publicKey;
    @Value("${push.vapid.private-key:}")
    private String privateKey;
    @Value("${push.vapid.subject:mailto:admin@raspisa.local}")
    private String subject;

    private nl.martijndwars.webpush.PushService client;

    public PushService(PushSubscriptionRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    void init() {

        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        if (publicKey.isBlank() || privateKey.isBlank()) {
            log.warn("VAPID-ключи не заданы — push-уведомления отключены.");
            return;
        }
        try {
            client = new nl.martijndwars.webpush.PushService(publicKey, privateKey, subject);
            log.info("Push-уведомления включены.");
        } catch (Exception e) {
            log.error("Не удалось инициализировать push (проверьте VAPID-ключи): {}", e.getMessage());
        }
    }

    public boolean isEnabled() {
        return client != null;
    }

    public String getPublicKey() {
        return publicKey;
    }

    @Transactional
    public void subscribe(PushSubscriptionRequest req) {
        PushSubscription sub = repo.findByEndpoint(req.endpoint()).orElseGet(PushSubscription::new);
        sub.setEndpoint(req.endpoint());
        sub.setP256dh(req.p256dh());
        sub.setAuth(req.auth());
        repo.save(sub);
    }

    @Async
    public void sendToAll(String title, String body, String url) {
        if (client == null) return;

        String payload = "{"
                + "\"title\":" + json(title == null || title.isBlank() ? "Расписание ШКИ" : title) + ","
                + "\"body\":" + json(body == null ? "" : body) + ","
                + "\"url\":" + json(url == null ? "/" : url)
                + "}";

        for (PushSubscription s : repo.findAll()) {
            try {
                Subscription sub = new Subscription(
                        s.getEndpoint(),
                        new Subscription.Keys(s.getP256dh(), s.getAuth())
                );
                int code = client.send(new Notification(sub, payload)).getStatusLine().getStatusCode();
                if (code == 404 || code == 410) {
                    repo.delete(s);
                }
            } catch (Exception e) {

                log.warn("Не удалось отправить push на {}: {}", s.getEndpoint(), e.getMessage());
            }
        }
    }

    private static String json(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        return sb.append("\"").toString();
    }
}
