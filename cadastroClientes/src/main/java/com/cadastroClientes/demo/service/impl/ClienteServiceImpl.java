package com.cadastroClientes.demo.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.model.repository.ClienteRepository;
import com.cadastroClientes.demo.service.ClienteService;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;
import com.cadastroClientes.demo.utils.Converters;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	@Autowired
	private ClienteRepository repository;

	@Override
	@Transactional
	public Cliente cadastrarCliente(Cliente cliente) {
		validarCliente(cliente);
		
		if (verificarEmail(cliente.getEmail())) {
			throw new RegraNegocioException("Email informado já foi cadastrado no sistema!");
		}
		
		if (verificarCpf(cliente.getCpf())) {
			throw new RegraNegocioException("CPF informado já foi cadastrado no sistema!");
		}
		
		return repository.save(cliente);
	}

	@Override
	@Transactional
	public Cliente atualizarCliente(Cliente cliente) {
		Objects.requireNonNull(cliente.getId());
		validarCliente(cliente);
		
		return repository.save(cliente);
	}	
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> buscarCliente(Cliente cliente) {
	    Example example = Example.of(cliente, 
	    		ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}
	
	@Override
	public boolean verificarEmail(String email) {
		return repository.existsByEmail(email);
	}
	
	@Override
	public boolean verificarCpf(String cpf) {
		return repository.existsByCpf(cpf);
	}
	
	public void validarCliente(Cliente cliente) {
		
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
