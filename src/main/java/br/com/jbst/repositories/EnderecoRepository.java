package br.com.jbst.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jbst.entities.map.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
	
	  Optional<Endereco> findByCepAndLogradouroAndComplementoAndNumeroAndBairroAndLocalidadeAndUf(
	            String cep, String logradouro, String complemento, String numero, String bairro, String localidade, String uf);

	  @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Endereco e WHERE e.unidadedetreinamento.id = :idUnidade")
	    boolean existsByUnidadedetreinamentoId(UUID idUnidade);

}
