package br.com.jbst.repositories;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jbst.entities.Curso;

public interface CursoRepository extends JpaRepository<Curso, UUID > {
	
	
	@Query("SELECT MAX(c.codigo) FROM Curso c")
    Integer findMaxNumeroCurso();
	
	 @Query("SELECT COUNT(c) > 0 FROM Curso c WHERE c.curso = :nomeCurso")
	    boolean existsByCurso(String nomeCurso);

	
	  @Query("SELECT c FROM Curso c WHERE c.curso = :curso AND c.descricao = :descricao AND c.conteudo = :conteudo AND c.modelo_certificado = :modeloCertificado AND c.campo_especifico = :campoEspecifico AND c.tituloautorizacao = :tituloautorizacao AND c.itemdaautorizacao = :itemdaautorizacao AND c.conteudodaautorizacao = :conteudodaautorizacao AND c.valorFormacao = :valorFormacao AND c.valorReciclagem = :valorReciclagem AND c.valorEad = :valorEad  AND c.composicaoOrcamentaria = :composicaoOrcamentaria AND c.observacoesGerais = :observacoesGerais")
	    List<Curso> findByAllFields(String curso, String descricao, String conteudo, String modeloCertificado, String campoEspecifico, String tituloautorizacao, String itemdaautorizacao, String conteudodaautorizacao, String valorFormacao, String valorReciclagem, String valorEad, String composicaoOrcamentaria, String observacoesGerais);
	                                                  
}
