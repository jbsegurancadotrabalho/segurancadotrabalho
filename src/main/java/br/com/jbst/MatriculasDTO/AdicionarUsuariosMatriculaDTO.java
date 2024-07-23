package br.com.jbst.MatriculasDTO;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class AdicionarUsuariosMatriculaDTO {

    private UUID idMatricula;
    private List<UUID> idsUsuarios;
    
}


