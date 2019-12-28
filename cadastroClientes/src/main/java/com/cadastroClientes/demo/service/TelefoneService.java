package com.cadastroClientes.demo.service;

import com.cadastroClientes.demo.model.entity.Telefone;

public interface TelefoneService {
	
	Telefone cadastrarTelefone(Telefone telefone);
	
	Telefone atualizarTelefone(Telefone telefone);

}
