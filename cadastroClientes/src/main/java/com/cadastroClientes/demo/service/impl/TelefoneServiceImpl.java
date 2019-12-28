package com.cadastroClientes.demo.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadastroClientes.demo.model.entity.Telefone;
import com.cadastroClientes.demo.model.repository.TelefoneRepository;
import com.cadastroClientes.demo.service.TelefoneService;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;
import com.cadastroClientes.demo.utils.Converters;

@Service
public class TelefoneServiceImpl implements TelefoneService {
	
	@Autowired
	private TelefoneRepository repository;

	@Override
	@Transactional
	public Telefone cadastrarTelefone(Telefone telefone) {
		validarTelefone(telefone);
		return repository.save(telefone);
	}

	@Override
	@Transactional
	public Telefone atualizarTelefone(Telefone telefone) {
		Objects.requireNonNull(telefone.getId());
		validarTelefone(telefone);
		
		return repository.save(telefone);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Telefone> buscarTelefone(Telefone telefone) {
		Example example = Example.of(telefone,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}
	
	public void validarTelefone(Telefone telefone) {
		
		if (telefone.getCliente() == null || telefone.getCliente().getId() == null) {
			throw new RegraNegocioException("É necessário vincular um Cliente válido");
		}
		
		if (telefone.getNumero() == null) {
			throw new RegraNegocioException("Campo Número não pode ser nulo");
		}
		
		if (telefone.getNumero().length() != 11) {
			throw new RegraNegocioException("É necessário fornecer um número de telefone com 11 dígitos");
		}
		
		if (!Converters.stringSomenteNumeros(telefone.getNumero())) {
			throw new RegraNegocioException("Só é permitido números no Campo Número");
		}
	}
}
