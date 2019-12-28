package com.cadastroClientes.demo.service;

import java.util.List;
import java.util.Optional;

import com.cadastroClientes.demo.model.entity.Telefone;

public interface TelefoneService {
	
	Telefone cadastrarTelefone(Telefone telefone);
	
	Telefone atualizarTelefone(Telefone telefone);
	
	List<Telefone> buscarTelefone(Telefone telefone);
	
	Optional<Telefone> buscarTelefonePorId(Long id);
}
