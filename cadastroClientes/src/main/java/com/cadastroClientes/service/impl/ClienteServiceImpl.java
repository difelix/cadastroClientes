package com.cadastroClientes.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadastroClientes.model.entity.Cliente;
import com.cadastroClientes.model.repository.ClienteRepository;
import com.cadastroClientes.service.ClienteService;
import com.cadastroClientes.service.exception.RegraNegocioException;
import com.cadastroClientes.utils.Converters;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	@Autowired
	private ClienteRepository repository;

	@Override
	@Transactional
	public Cliente cadastrarCliente(Cliente cliente) {
		validarCliente(cliente);
		return repository.save(cliente);
	}

	@Override
	@Transactional
	public Cliente atualizarCliente(Cliente cliente) {
		Objects.requireNonNull(cliente.getId());
		validarCliente(cliente);
		return repository.save(cliente);
	}	
	
	private void validarCliente(Cliente cliente) {
		
		if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
			throw new RegraNegocioException("Campo Nome não pode ser vazio");
		}
		
		if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
			throw new RegraNegocioException("Campo Email não pode ser vazio");
		}
		
		if (cliente.getCpf() == null) {
			throw new RegraNegocioException("Campo CPF não pode ser vazio");
		}
		
		if (cliente.getCpf().length() != 11) {
			throw new RegraNegocioException("Campo CPF precisa ter obrigatoriamente 11 números");
		}	
		
	    if (!Converters.stringSomenteNumeros(cliente.getCpf())) {
	    	throw new RegraNegocioException("Campo CPF é composto somente por números");
	    }
	}
}
