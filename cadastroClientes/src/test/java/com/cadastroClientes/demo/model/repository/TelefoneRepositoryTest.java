package com.cadastroClientes.demo.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.model.entity.Telefone;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TelefoneRepositoryTest {
	
	@Autowired
	TelefoneRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void salvarTelefoneComSucesso() {
		Cliente cliente = criarCliente();
		cliente = entityManager.persist(cliente);		
		Telefone telefone = criarTelefone(cliente);
		
		Telefone telefoneSalvo = repository.save(telefone);
		
		Assertions.assertThat(telefoneSalvo.getId()).isNotNull();
	}
	
	@Test
	public void atualizarTelefoneComSucesso() {
		Cliente cliente = criarCliente();
		cliente = entityManager.persist(cliente);	
		Telefone telefone = criarTelefone(cliente);
		
		telefone = entityManager.persist(telefone);
		telefone.setNumero("21111112222");
		
		Telefone telefoneAtualizado = repository.save(telefone);
		
		Assertions.assertThat(telefoneAtualizado.getNumero()).isEqualTo(telefone.getNumero());	
	}
	
	@Test
	public void buscarTelefoneComIdExistente() {
		Cliente cliente = criarCliente();
		cliente = entityManager.persist(cliente);
		Telefone telefone = criarTelefone(cliente);
		telefone = entityManager.persist(telefone);
		
		Optional<Telefone> resultadoBusca = repository.findById(telefone.getId());
		
		Assertions.assertThat(resultadoBusca.isPresent()).isTrue();
	}
	
	@Test
	public void buscarTelefoneComIdInexistente() {
		Optional<Telefone> resultadoBusca = repository.findById(10000L);
		
		Assertions.assertThat(resultadoBusca.isPresent()).isFalse();
	}
	
	private Cliente criarCliente() {
		return Cliente.builder()
				.cpf("11111111111")
				.email("teste@email.com")
				.nome("Teste")
				.build();
	}
	
	private Telefone criarTelefone(Cliente cliente) {
		return Telefone.builder()
				.numero("19111112222")
				.cliente(cliente)
				.build();
	}

}
