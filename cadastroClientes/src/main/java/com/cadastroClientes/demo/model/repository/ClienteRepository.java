package com.cadastroClientes.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadastroClientes.demo.model.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
     boolean existsByEmail(String email);
     
     boolean existsByCpf(String cpf);
}
