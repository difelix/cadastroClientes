package com.cadastroClientes.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadastroClientes.demo.model.entity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
