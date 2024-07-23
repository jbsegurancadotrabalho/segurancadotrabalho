package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.Pedidos;

public interface PedidosRepository extends JpaRepository<Pedidos, UUID >{

	
    boolean existsByNumerodopedidoAndVendaAndNotafiscal(String numerodopedido, String venda, String notafiscal);

	
	
}
