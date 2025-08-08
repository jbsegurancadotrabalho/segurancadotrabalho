package br.com.jbst.DTO;



import lombok.Data;

@Data
public class SendMessageZapDTO {
    private String phone;
    private String message;

    public SendMessageZapDTO() {}

    public SendMessageZapDTO(String phone, String message) {
        this.phone = phone;
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
