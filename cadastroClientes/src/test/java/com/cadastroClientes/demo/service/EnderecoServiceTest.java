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
import com.cadastroClientes.demo.model.entity.Endereco;
import com.cadastroClientes.demo.model.repository.EnderecoRepository;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;
import com.cadastroClientes.demo.service.impl.EnderecoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EnderecoServiceTest {
	
	@MockBean
	EnderecoRepository repository;
	
	@SpyBean
	EnderecoServiceImpl serviceImpl;
	
	@Test
	public void cadastrarEnderecoComSucesso() {
		Cliente cliente = criarCliente();
		Endereco endereco = criarEndereco();
		endereco.setCliente(cliente);
		
		Mockito.doNothing().when(serviceImpl).validarEndereco(Mockito.any(Endereco.class));
		Mockito.when(repository.save(Mockito.any(Endereco.class))).thenReturn(endereco);
		
		Endereco enderecoSalvo = serviceImpl.cadastrarEndereco(endereco);
		
		Assertions.assertThat(enderecoSalvo.getId()).isNotNull();
		Assertions.assertThat(enderecoSalvo).isEqualTo(endereco);
	}
	
	@Test
	public void validarCadastroEnderecoComClienteInexistente() {
		Endereco endereco = criarEndereco();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarEndereco(endereco));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("É necessário vincular um cliente válido");
	}
	
	@Test
	public void validarEnderecoComCampoRuaVazio() {
		Endereco endereco = criarEndereco();
		endereco.setCliente(criarCliente());
		endereco.setRua(null);
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarEndereco(endereco));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo Rua não pode ser vazio");
	}
	
	@Test
	public void validarEnderecoComCampoCidadeVazio() {
		Endereco endereco = criarEndereco();
		endereco.setCliente(criarCliente());
		endereco.setCidade(null);
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarEndereco(endereco));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo Cidade não pode ser vazio");
	}
	
	@Test
	public void validarEnderecoComCampoBairroVazio() {
		Endereco endereco = criarEndereco();
		endereco.setCliente(criarCliente());
		endereco.setBairro(null);;
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarEndereco(endereco));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo Bairro não pode ser vazio");
	}
	
	@Test
	public void validarEnderecoComCampoEstadoVazio() {
		Endereco endereco = criarEndereco();
		endereco.setCliente(criarCliente());
		endereco.setEstado(null);;
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarEndereco(endereco));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo Estado não pode ser vazio");
	}
	
	@Test
	public void validarEnderecoComCampoCepInvalido() {
		String cep = "0000000000";
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.validarCep(cep));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo CEP deve conter obrigatoriamente 8 dígitos numéricos");
	}
	
	@Test
	public void validarEnderecoCampoCepComCaracteresInvalidos() {
		String cep = "0s0d0f0h";
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.validarCep(cep));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo CEP deve conter obrigatoriamente dígitos numéricos");
	}
	
	public Cliente criarCliente() {
		return Cliente.builder()
				.nome("Diego")
				.cpf("00000000000")
				.email("diego@email.com")
				.id(1L)
				.build();
	}
	
	public Endereco criarEndereco() {
		return Endereco.builder()
				.rua("Rua 15")
				.cidade("São Paulo")
				.estado("SP")
				.bairro("Bairro")
				.cep("11111111")
				.id(1L)
				.build();
	}

}
