package com.tangerineteam.gyoolworksap.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class RsaController {

    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("keys/public.pem");
        String publicKey = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> response = new HashMap<>();
        response.put("publicKey", publicKey);
        return response;
    }
}
