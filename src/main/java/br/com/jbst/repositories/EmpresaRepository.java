package br.com.jbst.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.map.Empresa;

public interface EmpresaRepository extends JpaRepository <Empresa, UUID>{
    List<Empresa> findByUsuario_Id(UUID idUsuario);

}
