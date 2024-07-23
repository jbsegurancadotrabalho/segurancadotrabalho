package br.com.jbst.DTO;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PutUnidadeDeTreinamentoDTO {

    private UUID idUnidadedetreinamento;
    private Instant dataHoraCriacao;
    private String unidadedetreinamento;
    private String cnpj;
    private String unidade;

}
