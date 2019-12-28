package com.cadastroClientes.demo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadastroClientes.demo.model.entity.Endereco;
import com.cadastroClientes.demo.model.repository.EnderecoRepository;
import com.cadastroClientes.demo.service.EnderecoService;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;
import com.cadastroClientes.demo.utils.Converters;

@Service
public class EnderecoServiceImpl implements EnderecoService {
	
	@Autowired
	private EnderecoRepository repository;

	@Override
	@Transactional
	public Endereco cadastrarEndereco(Endereco endereco) {
		validarEndereco(endereco);
		return repository.save(endereco);
	}

	@Override
	public Endereco atualizarEndereco(Endereco endereco) {
		Objects.requireNonNull(endereco.getId());
		validarEndereco(endereco);
		return repository.save(endereco);
	}
	
	public void validarEndereco(Endereco endereco) {
		
		if (endereco.getCliente() == null || endereco.getCliente().getId() == null) {
			throw new RegraNegocioException("É necessário vincular um cliente válido");
		}
		
		if (endereco.getRua() == null) {
			throw new RegraNegocioException("Campo Rua não pode ser vazio");
		}
		
		if (endereco.getCidade() == null) {
			throw new RegraNegocioException("Campo Cidade não pode ser vazio");
		}
		
		if (endereco.getBairro() == null) {
			throw new RegraNegocioException("Campo Bairro não pode ser vazio");
		}
		
		if (endereco.getEstado() == null) {
			throw new RegraNegocioException("Campo Estado não pode ser vazio");
		}
		
		if (endereco.getCep() == null) {
			throw new RegraNegocioException("Campo CEP não pode ser vazio");
		}
		
		if (endereco.getCep().length() != 8) {
			throw new RegraNegocioException("Campo CEP deve conter obrigatoriamente 8 dígitos numéricos");
		}
		
		if (!Converters.stringSomenteNumeros(endereco.getCep())) {
			throw new RegraNegocioException("Campo CEP deve conter obrigatoriamente dígitos numéricos");
		}
	}

}
