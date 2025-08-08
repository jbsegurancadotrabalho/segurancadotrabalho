package br.com.jbst.components;



import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import br.com.jbst.DTO.SendMessageZapDTO;

@Component
public class ZApiSenderComponent {

    private static final Logger logger = LoggerFactory.getLogger(ZApiSenderComponent.class);
    private static final String ZAPI_URL_TEMPLATE = "https://api.z-api.io/instances/3E0D84E4E0C2C057E5C03EF0F02679BC/token/2AD64B2E3E713B053DCDE808/send-text";

    @Value("${zapi.instance.id}")
    private String instanceId;

    @Value("${zapi.client.token}")
    private String clientToken;

    private final RestTemplate restTemplate;

    public ZApiSenderComponent(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(SendMessageZapDTO dto) {
        if (!isValidDto(dto)) {
            logger.warn("❗ Dados inválidos: número ou mensagem ausentes.");
            return;
        }

        String url = buildZApiUrl();
        HttpEntity<SendMessageZapDTO> request = buildHttpRequest(dto);

        logger.info("📲 Enviando WhatsApp para {}", dto.getPhone());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            logger.info("📨 Status: {}, Resposta: {}", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            logger.error("❌ Erro ao enviar WhatsApp para {}: {}", dto.getPhone(), e.getMessage(), e);
        }
    }

    private boolean isValidDto(SendMessageZapDTO dto) {
        return dto != null && StringUtils.hasText(dto.getPhone()) && StringUtils.hasText(dto.getMessage());
    }

    private String buildZApiUrl() {
        return String.format(ZAPI_URL_TEMPLATE, instanceId);
    }

    private HttpEntity<SendMessageZapDTO> buildHttpRequest(SendMessageZapDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-Token", clientToken);
        return new HttpEntity<>(dto, headers);
    }
}
