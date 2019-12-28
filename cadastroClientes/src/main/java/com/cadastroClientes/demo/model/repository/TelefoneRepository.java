package com.cadastroClientes.demo.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadastroClientes.demo.model.entity.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
     
	Optional<Telefone> findById(Long id);
}
