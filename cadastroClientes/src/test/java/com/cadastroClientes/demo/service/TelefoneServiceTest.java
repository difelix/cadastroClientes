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
import com.cadastroClientes.demo.model.entity.Telefone;
import com.cadastroClientes.demo.model.repository.TelefoneRepository;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;
import com.cadastroClientes.demo.service.impl.TelefoneServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TelefoneServiceTest {
	
	@MockBean
	TelefoneRepository repository;
	
	@SpyBean
	TelefoneServiceImpl serviceImpl;
	
	@Test
	public void cadastrarTelefoneComSucesso() {
		Cliente cliente = montarCliente();
		Telefone telefone = montarTelefone(cliente);
		
		Mockito.doNothing().when(serviceImpl).validarTelefone(Mockito.any(Telefone.class));
		Mockito.when(repository.save(Mockito.any(Telefone.class))).thenReturn(telefone);
		
		Telefone telefoneSalvo = serviceImpl.cadastrarTelefone(telefone);
		
		Assertions.assertThat(telefoneSalvo.getId()).isNotNull();
		Assertions.assertThat(telefoneSalvo.getNumero()).isEqualTo(telefone.getNumero());
		Assertions.assertThat(telefoneSalvo.getCliente()).isEqualTo(telefone.getCliente());
	}
	
	@Test
	public void cadastrarTelefoneSemCliente() {
		Telefone telefone = Telefone.builder().numero("19222221111").build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarTelefone(telefone));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("É necessário vincular um Cliente válido");
	}
	
	@Test
	public void cadastrarTelefoneSemNumero() {
		Cliente cliente = montarCliente();
		Telefone telefone = Telefone.builder().cliente(cliente).build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarTelefone(telefone));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Campo Número não pode ser nulo");
	}
	
	@Test
	public void cadastrarTelefoneComCaracteresNoNumero() {
		Cliente cliente = montarCliente();
		Telefone telefone = Telefone.builder().cliente(cliente).numero("1911111444s").build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarTelefone(telefone));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Só é permitido números no Campo Número");
	}
	
	@Test
	public void cadastrarTelefoneComQuantidadeDigitosErrado() {
		Cliente cliente = montarCliente();
		Telefone telefone = Telefone.builder().cliente(cliente).numero("1911111").build();
		
		Throwable exception = Assertions.catchThrowable(() -> serviceImpl.cadastrarTelefone(telefone));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("É necessário fornecer um número de telefone com 11 dígitos");
	}
	
	@Test(expected = NullPointerException.class)
	public void atualizarTelefoneSemInformarId() {
		Cliente cliente = montarCliente();
		Telefone telefone = Telefone.builder().cliente(cliente).numero("19111112222").build();
		
		serviceImpl.atualizarTelefone(telefone);
	}
	
	public Telefone montarTelefone(Cliente cliente) {
		return Telefone.builder()
				.cliente(cliente)
				.numero("19111112222")
				.id(1L)
				.build();
	}
	
	public Cliente montarCliente() {
		return Cliente.builder()
				.nome("Diego Félix")
				.cpf("00000000000")
				.email("diego.felix@email.com")
				.id(1L)
				.build();
	}
}
