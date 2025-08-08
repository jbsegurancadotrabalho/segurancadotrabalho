package br.com.jbst.DTO;

import java.util.UUID;

import lombok.Data;

@Data

public class GetAssinaturaAlunoUltimaDTO {
    private UUID id_assinatura_aluno_D4Sign;
    private byte[] imagem;
}
