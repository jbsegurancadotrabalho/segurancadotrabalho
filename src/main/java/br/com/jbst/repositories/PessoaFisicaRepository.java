package br.com.jbst.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.map.Empresa;
import br.com.jbst.entities.map.PessoaFisica;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, UUID> {
    
    List<PessoaFisica> findByUsuarioId(UUID usuarioId);

}
