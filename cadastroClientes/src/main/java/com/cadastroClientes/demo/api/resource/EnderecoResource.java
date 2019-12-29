package com.cadastroClientes.demo.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.client.RestTemplate;

import com.cadastroClientes.demo.api.dto.EnderecoDto;
import com.cadastroClientes.demo.api.dto.consumingrest.EnderecoConsuming;
import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.model.entity.Endereco;
import com.cadastroClientes.demo.service.ClienteService;
import com.cadastroClientes.demo.service.EnderecoService;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enderecos")
@RequiredArgsConstructor
public class EnderecoResource {

	private final EnderecoService enderecoService;
	private final ClienteService clienteService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping
	public ResponseEntity salvarEndereco(@RequestBody EnderecoDto dto) {
		try {
		    Endereco endereco = convertDto(dto);
		    endereco = getEnderecoData(endereco);
		    endereco = enderecoService.cadastrarEndereco(endereco);
		    return new ResponseEntity(endereco, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizarEndereco(@PathVariable Long id, @RequestBody EnderecoDto dto) {
		return enderecoService.buscarEnderecoPorId(id).map(entity -> {
			try {
				Endereco endereco = convertDto(dto);
				endereco.setId(entity.getId());
				endereco = getEnderecoData(endereco);
				endereco = enderecoService.atualizarEndereco(endereco);
				return new ResponseEntity(endereco, HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Id de endereço não foi encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping("/filtrar")
	public ResponseEntity filtrarEndereco(@RequestParam Long idCliente) {
		Endereco endereco = new Endereco();
		
		Optional<Cliente> cliente = clienteService.buscarClientePorId(idCliente);
		if (cliente.isPresent()) {
			endereco.setCliente(cliente.get());
		} else {
			return ResponseEntity.badRequest().body("Cliente informado não foi encontrado");
		}
		
		List<Endereco> enderecosFiltrados = enderecoService.buscarEndereco(endereco);
		return new ResponseEntity(enderecosFiltrados, HttpStatus.OK);
	}
	
	public Endereco getEnderecoData(Endereco endereco) {
		EnderecoConsuming enderecoConsuming = restTemplate.getForObject(
				"http://viacep.com.br/ws/" + endereco.getCep() + "/json/", EnderecoConsuming.class);
		
		if (enderecoConsuming.isErro()) {
			throw new RegraNegocioException("CEP informado não foi encontrado!");
		}
		
		endereco.setBairro(enderecoConsuming.getBairro());
		endereco.setEstado(enderecoConsuming.getUf());
		endereco.setCidade(enderecoConsuming.getLocalidade());
		endereco.setRua(enderecoConsuming.getLogradouro());
		   
		return endereco;
	}
	
	public Endereco convertDto(EnderecoDto dto) {
		Endereco endereco = new Endereco();
		
		if (dto.getCep() != null) {
		    enderecoService.validarCep(dto.getCep());
			endereco.setCep(dto.getCep());
		} else {
			throw new RegraNegocioException("CEP não pode ser nulo");
		}
		
		if (dto.getComplemento() != null) {
			endereco.setComplemento(dto.getComplemento());
		}
		
		if (dto.getReferencia() != null) {
			endereco.setReferencia(dto.getReferencia());
		}
		
		if (dto.getIdCliente() != null) {
			Optional<Cliente> cliente = clienteService.buscarClientePorId(dto.getIdCliente());
			if (cliente.isPresent()) {
				endereco.setCliente(cliente.get());
			} else {
				throw new RegraNegocioException("Cliente informado não foi encontrado na base de dados");
			}
		}
		
		return endereco;
	}
}
