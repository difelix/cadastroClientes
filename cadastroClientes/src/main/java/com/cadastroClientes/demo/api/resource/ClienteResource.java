package com.cadastroClientes.demo.api.resource;

import java.util.List;

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

import com.cadastroClientes.demo.api.dto.ClienteDto;
import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.service.ClienteService;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteResource {
	
	private final ClienteService service;
	
	@PostMapping
	public ResponseEntity cadastrarCliente(@RequestBody ClienteDto dto) {
		try {
		    Cliente cliente = converterDto(dto);
		    cliente = service.cadastrarCliente(cliente);
		    return new ResponseEntity(cliente, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizarCadastroCliente(@PathVariable Long id , @RequestBody ClienteDto dto) {
		return service.buscarClientePorId(id).map(entity -> {
			try {
				Cliente cliente = converterDto(dto);
				cliente.setId(entity.getId());
				cliente = service.atualizarCliente(cliente);
				return new ResponseEntity(cliente, HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> ResponseEntity.badRequest().body("Cliente informado não foi encontrado na base de dados"));
	}
	
	@GetMapping("/checkemail")
	public ResponseEntity verificarEmailExiste(@RequestParam String email) {
		boolean exists = service.verificarEmail(email);
		if (exists) {
			return new ResponseEntity("Email " + email + " existe no sistema", HttpStatus.FOUND);
		}
		return new ResponseEntity("Email " + email + " não existe no sistema", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/searchemail")
	public ResponseEntity buscarCadastroPorEmail(@RequestParam String email) {
		Cliente clienteFilter = Cliente.builder().email(email).build();
		List<Cliente> clientesEncontrados = service.buscarCliente(clienteFilter);
		return new ResponseEntity(clientesEncontrados, HttpStatus.OK);
	}
	
	private Cliente converterDto(ClienteDto dto) {
		Cliente cliente = new Cliente();
	    
		if (dto.getNome() != null) {
			cliente.setNome(dto.getNome());
		}
		
		if (dto.getCpf() != null) {
			cliente.setCpf(dto.getCpf());
		}
		
		if (dto.getEmail() != null) {
			cliente.setEmail(dto.getEmail());
		}
		
		return cliente;
	}	
}
