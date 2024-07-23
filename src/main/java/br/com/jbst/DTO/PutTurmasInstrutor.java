package br.com.jbst.DTO;



import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class PutTurmasInstrutor {    
    private UUID idTurmas;
    private List<UUID> idinstrutor;
}
