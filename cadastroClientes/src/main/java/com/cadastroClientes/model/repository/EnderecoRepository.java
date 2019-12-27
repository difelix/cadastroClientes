package com.cadastroClientes.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadastroClientes.model.entity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
