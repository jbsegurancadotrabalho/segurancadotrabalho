package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.Evidencias;

public interface EvidenciasRepository extends JpaRepository<Evidencias, UUID> {

}
