package br.com.jbst.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.Faturamento;
import br.com.jbst.entities.FaturamentoPf;

public interface FaturamentopfRepository extends JpaRepository<FaturamentoPf, UUID> {

    @Query("SELECT MAX(f.numeroFaturamento) FROM FaturamentoPf f")
    Integer findMaxNumeroFaturamento();

    @Query("SELECT CASE WHEN COUNT(pf.idpessoafisica) > 0 THEN true ELSE false END " +
           "FROM PessoaFisica pf " +
           "WHERE pf.idpessoafisica = :idpessoafisica " +
           "AND ((pf.dataHoraCriacao >= :dataInicio AND pf.dataHoraCriacao <= :dataFim) OR " +
           "(pf.dataHoraCriacao >= :dataInicio AND pf.dataHoraCriacao <= :dataFim) OR " +
           "(pf.dataHoraCriacao <= :dataInicio AND pf.dataHoraCriacao >= :dataFim))")
    boolean existsPessoaFisicaNoPeriodo(@Param("idpessoafisica") UUID idpessoafisica,
                                        @Param("dataInicio") Instant dataInicio,
                                        @Param("dataFim") Instant dataFim);
    
    
    

}
