package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.map.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, UUID> {

}
