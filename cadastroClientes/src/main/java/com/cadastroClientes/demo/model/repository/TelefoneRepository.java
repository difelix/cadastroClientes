package com.cadastroClientes.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadastroClientes.demo.model.entity.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

}
