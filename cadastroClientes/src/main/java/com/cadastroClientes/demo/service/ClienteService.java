package com.cadastroClientes.demo.service;

import java.util.List;
import com.cadastroClientes.demo.model.entity.Cliente;

public interface ClienteService {

	Cliente cadastrarCliente(Cliente cliente);
	
	Cliente atualizarCliente(Cliente cliente);
	
	boolean verificarEmail(String email);
	
	boolean verificarCpf(String cpf);
	
	List<Cliente> buscarCliente(Cliente cliente);
}
