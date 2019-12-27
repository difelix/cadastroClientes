package com.cadastroClientes.service;

import com.cadastroClientes.model.entity.Cliente;

public interface ClienteService {

	Cliente cadastrarCliente(Cliente cliente);
	
	Cliente atualizarCliente(Cliente cliente);
}
