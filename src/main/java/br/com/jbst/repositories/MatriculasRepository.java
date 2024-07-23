package br.com.jbst.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Turmas;
import br.com.jbst.entities.map.Funcionario;
import br.com.jbst.entities.map.PessoaFisica;

public interface MatriculasRepository extends JpaRepository<Matriculas, UUID > {

	
	@Query("SELECT MAX(m.numeroMatricula) FROM Matriculas m")
    Integer findMaxNumeroMatricula();
	
	
	@Query(
			"select m from Matriculas m "
		  + "order by m.numeroMatricula"
	)
	List<Matriculas> findAllMatriculas();
	
	
	@Query(
     "SELECT m, f FROM Matriculas m\n"
      + "LEFT JOIN m.funcionario f\n"		 
	)
	List<Matriculas> findAllMatricula();

    List<Matriculas> findByPessoafisica_Idpessoafisica(UUID idpessoafisica);
    
    List<Matriculas> findByFaturamento_Idfaturamento(UUID idFaturamento);

    @Query("SELECT m FROM Matriculas m JOIN m.usuarios u WHERE u.id = :usuarioId")
    List<Matriculas> findByUsuarioId(@Param("usuarioId") UUID usuarioId);

    boolean existsByFuncionarioAndTurmas(Funcionario funcionario, Turmas turma);
    
 // Change 'PessoaFisica' to 'pessoafisica' in the method name
    boolean existsByPessoafisicaAndTurmas(PessoaFisica pessoafisica, Turmas turmas);

    boolean existsByTurmasAndPessoafisica_Cpf(Turmas turma, String cpf);

}


