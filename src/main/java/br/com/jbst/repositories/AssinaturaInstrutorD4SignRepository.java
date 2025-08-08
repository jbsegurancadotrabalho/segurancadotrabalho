package br.com.jbst.repositories;


import br.com.jbst.entities.AssinaturaInstrutorD4Sign;
import br.com.jbst.entities.Turmas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssinaturaInstrutorD4SignRepository extends JpaRepository<AssinaturaInstrutorD4Sign, UUID> {

    // Buscar todas as assinaturas de uma turma, ordenadas pela ordem
    List<AssinaturaInstrutorD4Sign> findByTurmaOrderByOrdemAsc(Turmas turma);

    // Ou por ID da turma diretamente (alternativa)
    List<AssinaturaInstrutorD4Sign> findByTurma_IdTurmasOrderByOrdemAsc(UUID idTurma);
}

