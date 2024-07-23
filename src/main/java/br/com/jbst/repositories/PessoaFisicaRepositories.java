package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.map.PessoaFisica;

public interface PessoaFisicaRepositories extends JpaRepository<PessoaFisica, UUID>{

}
