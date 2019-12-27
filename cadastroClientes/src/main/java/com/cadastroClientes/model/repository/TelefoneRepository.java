package com.cadastroClientes.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadastroClientes.model.entity.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

}
