package br.com.jbst.repositories;


import java.util.List;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.AssinaturaAlunoD4Sign;
import br.com.jbst.entities.Matriculas;

public interface IAssinaturaAlunoD4SignRepository extends JpaRepository<AssinaturaAlunoD4Sign, UUID> {

    // Buscar todas as assinaturas de uma turma, ordenadas pela ordem
    List<AssinaturaAlunoD4Sign> findByMatriculaOrderByOrdemAsc(Matriculas matricula);

    // Ou por ID da turma diretamente (alternativa)
    List<AssinaturaAlunoD4Sign> findByMatricula_IdMatriculaOrderByOrdemAsc(UUID idMatricula);

}
