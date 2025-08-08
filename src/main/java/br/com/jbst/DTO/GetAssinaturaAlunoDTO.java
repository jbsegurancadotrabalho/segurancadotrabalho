package br.com.jbst.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class GetAssinaturaAlunoDTO {
    private UUID id_assinatura_aluno_D4Sign;
    private int ordem;
    private byte[] imagem;
}
