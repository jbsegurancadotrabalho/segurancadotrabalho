package br.com.jbst.DTO;


import lombok.Data;

@Data
public class WebhookMessageZapDTO {
    private String phone;
    private String messageId;
    private String status;
    private String chatName;
    private String type;
    private Text text;

    @Data
    public static class Text {
        private String message;
    }
}

