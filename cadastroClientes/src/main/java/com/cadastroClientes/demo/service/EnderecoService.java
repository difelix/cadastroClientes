package com.cadastroClientes.demo.service;

import java.util.List;
import java.util.Optional;

import com.cadastroClientes.demo.model.entity.Endereco;

public interface EnderecoService {
	
	Endereco cadastrarEndereco(Endereco endereco);
	
	Endereco atualizarEndereco(Endereco endereco);
	
	void validarCep(String cep);
	
	Optional<Endereco> buscarEnderecoPorId(Long id);
	
	List<Endereco> buscarEndereco(Endereco endereco);
}
