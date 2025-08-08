package br.com.jbst.components;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class OnlySocialClient {

    @Value("${onlysocial.api.token}")
    private String token;

    @Value("${onlysocial.api.workspaceUuid}")
    private String workspaceUuid;

    @Value("${onlysocial.api.accountIds}")
    private String accountIds; // exemplo: "42174,42175,42176"

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarPostTurma(String texto, int mediaId) {
        String url = "https://app.onlysocial.io/os/api/" + workspaceUuid + "/posts";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        List<Integer> contas = Arrays.stream(accountIds.split(","))
                .map(String::trim)
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        Map<String, Object> request = new HashMap<>();
        request.put("accounts", contas);
        request.put("tags", List.of());
        request.put("schedule_now", true);
        request.put("queue", false);
        request.put("schedule", false);

        Map<String, Object> content = new HashMap<>();
        content.put("body", texto);
        content.put("media", List.of(mediaId)); // ✅ vinculando o vídeo ao post

        Map<String, Object> version = new HashMap<>();
        version.put("account_id", 0); // versão original
        version.put("is_original", true);
        version.put("content", List.of(content));

        request.put("versions", List.of(version));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            System.out.println("✅ Post com mídia enviado para OnlySocial: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("❌ Erro ao enviar para OnlySocial: " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        }
    }


}
