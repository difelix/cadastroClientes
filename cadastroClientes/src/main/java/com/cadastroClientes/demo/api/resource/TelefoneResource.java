package com.cadastroClientes.demo.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cadastroClientes.demo.api.dto.TelefoneDto;
import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.model.entity.Telefone;
import com.cadastroClientes.demo.service.ClienteService;
import com.cadastroClientes.demo.service.TelefoneService;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/telefones")
@RequiredArgsConstructor
public class TelefoneResource {
	
	private final ClienteService clienteService;
	private final TelefoneService telefoneService;
	
	@PostMapping
	public ResponseEntity salvarTelefone(@RequestBody TelefoneDto dto) {
		try {
			Telefone telefone = converterDto(dto);
			telefone = telefoneService.cadastrarTelefone(telefone);
			return new ResponseEntity(telefone, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizarTelefone(@PathVariable Long id, @RequestBody TelefoneDto dto) {
		return telefoneService.buscarTelefonePorId(id).map(entity -> {
			try {
				Telefone telefone = converterDto(dto);
				telefone.setId(entity.getId());
				telefone = telefoneService.atualizarTelefone(telefone);
				return new ResponseEntity(telefone, HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Telefone informado não foi encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity buscarTelefone(@RequestParam String numero,
			@RequestParam Long idCliente) {
		Telefone telefoneFilter = new Telefone();
		telefoneFilter.setNumero(numero);
		
		Optional<Cliente> cliente = clienteService.buscarClientePorId(idCliente);
		if (cliente.isPresent()) {
			telefoneFilter.setCliente(cliente.get());
		} else {
			return ResponseEntity.badRequest().body("Não existe numero de telefone vinculado com cliente informado");
		}
		
		List<Telefone> telefonesEncontrados = telefoneService.buscarTelefone(telefoneFilter);
		return new ResponseEntity(telefonesEncontrados, HttpStatus.OK);
	}
	
	public Telefone converterDto(TelefoneDto dto) {
		Telefone telefone = new Telefone();
		
		if (dto.getNumero() != null) {
			telefone.setNumero(dto.getNumero());
		}
		
		if (dto.getIdCliente() != null) {
		    Optional<Cliente> cliente = clienteService.buscarClientePorId(dto.getIdCliente());
		    if (!cliente.isPresent()) {
		    	throw new RegraNegocioException("Cliente informado não foi encontrado na base de dados");
		    } else {
		        telefone.setCliente(cliente.get());
		    }
		}
		
		return telefone;
	}

}
