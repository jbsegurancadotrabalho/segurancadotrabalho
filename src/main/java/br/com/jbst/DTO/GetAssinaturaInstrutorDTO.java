package br.com.jbst.DTO;


import lombok.Data;
import java.util.UUID;

@Data
public class GetAssinaturaInstrutorDTO {
    private UUID id_assinatura_instrutor_D4Sign;
    private int ordem;
    private byte[] imagem;
}
