package com.cadastroClientes.demo.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.model.repository.ClienteRepository;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;
import com.cadastroClientes.demo.service.impl.ClienteServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClienteServiceTest {
	
	@MockBean
	ClienteRepository repository;
	
	@SpyBean
	ClienteServiceImpl serviceImpl;
	
	@Test
	public void cadastrarClienteComSucesso() {
		// cenario
		Cliente cliente = Cliente.builder()
				.nome("Diego Félix")
				.cpf("00000000000")
				.email("diego.felix@email.com")
				.id(1L)
				.build();
		
		Mockito.doNothing().when(serviceImpl).validarCliente(Mockito.any(Cliente.class));
		Mockito.when(repository.save(Mockito.any(Cliente.class))).thenReturn(cliente);
		
		// execucao
		Cliente clienteSalvo = serviceImpl.cadastrarCliente(cliente);
		
		// validacao
		Assertions.assertThat(clienteSalvo.getId()).isNotNull();
		Assertions.assertThat(clienteSalvo.getEmail()).isEqualTo(cliente.getEmail());
		Assertions.assertThat(clienteSalvo.getCpf()).isEqualTo(cliente.getCpf());
		Assertions.assertThat(clienteSalvo.getNome()).isEqualTo(cliente.getNome());
	}
	
	@Test
	public void cadastrarClienteSemNome() {
		// cenario
		Cliente cliente = Cliente.builder()
				.cpf("00000000000")
				.email("diego@email.com")
				.build();
		
		// execucao
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarCliente(cliente));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo Nome não pode ser vazio");
	}
	
	@Test
	public void cadastrarClienteSemEmail() {
		Cliente cliente = Cliente.builder()
				.cpf("00000000000")
				.nome("Diego")
				.build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarCliente(cliente));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo Email não pode ser vazio");
	}
	
	@Test
	public void cadastrarClienteSemCpf() {
		Cliente cliente = Cliente.builder()
				.email("diego@email.com")
				.nome("Diego")
				.build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarCliente(cliente));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo CPF não pode ser vazio");
	}
	
	@Test
	public void cadastrarClienteComCpfInvalido() {
		Cliente cliente = Cliente.builder()
				.cpf("000000000000000000")
				.email("diego@email.com")
				.nome("Diego")
				.build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarCliente(cliente));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo CPF precisa ter obrigatoriamente 11 números");
	}
	
	@Test
	public void cadastrarClienteCpfComLetras() {
		Cliente cliente = Cliente.builder()
				.cpf("0000000000s")
				.email("diego@email.com")
				.nome("Diego")
				.build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarCliente(cliente));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo CPF é composto somente por números");
	}
	
	@Test
	public void atualizarClienteComSucesso() {
		Cliente cliente = Cliente.builder()
				.id(1L)
				.cpf("00000000000")
				.email("diego@email.com")
				.nome("Diego")
				.build();
		
		Mockito.doNothing().when(serviceImpl).validarCliente(Mockito.any(Cliente.class));
		Mockito.when(repository.save(Mockito.any(Cliente.class))).thenReturn(cliente);
		
		Cliente clienteAtualizado = serviceImpl.atualizarCliente(cliente);
		
		Assertions.assertThat(clienteAtualizado.getId()).isNotNull();
		Assertions.assertThat(clienteAtualizado.getCpf()).isEqualTo(cliente.getCpf());
		Assertions.assertThat(clienteAtualizado.getEmail()).isEqualTo(cliente.getEmail());
		Assertions.assertThat(clienteAtualizado.getNome()).isEqualTo(cliente.getNome());
	}
	
	@Test(expected = NullPointerException.class)
	public void tentarAtualizarClienteSemFornecerId() {
		Cliente cliente = Cliente.builder()
				.cpf("00000000000")
				.email("diego@email.com")
				.nome("Diego")
				.build();
		
		serviceImpl.atualizarCliente(cliente);
	}
	
	@Test
	public void verificarExistenciaDeEmailCadastrado() {
		String email = "diego@email.com";
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		boolean exists = serviceImpl.verificarEmail(email);
		
		Assertions.assertThat(exists).isTrue();
	}

}
