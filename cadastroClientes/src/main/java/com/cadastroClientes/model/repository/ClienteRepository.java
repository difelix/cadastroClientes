package com.cadastroClientes.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadastroClientes.model.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
