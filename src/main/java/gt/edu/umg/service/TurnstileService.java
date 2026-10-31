package gt.edu.umg.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TurnstileService {

    @Value("${cloudflare.turnstile.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    public boolean validateToken(String token, String clientIp) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", secretKey);
        body.add("response", token);
        if (clientIp != null && !clientIp.isEmpty()) {
            body.add("remoteip", clientIp);
        }

        try {
            // Hacemos la petición POST a Cloudflare
            Map response = restTemplate.postForObject(VERIFY_URL, body, Map.class);
            
            // Cloudflare devuelve un JSON con "success": true o false
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error validando Turnstile: " + e.getMessage());
        }
        return false;
    }
}