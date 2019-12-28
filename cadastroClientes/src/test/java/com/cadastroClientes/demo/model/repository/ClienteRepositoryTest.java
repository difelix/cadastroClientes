package com.cadastroClientes.demo.model.repository;

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
import com.cadastroClientes.demo.model.repository.ClienteRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClienteRepositoryTest {
	
	@Autowired
	ClienteRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void verificarSeCadastroFoiEfetivado() {
		// cenario
		Cliente cliente = montarObjeto();
		
		// execucao
		cliente = repository.save(cliente);
		
		// validacao
		Assertions.assertThat(cliente.getId()).isNotNull();
	}
	
	@Test
	public void verificarSeAtualizacaoCadastroFoiEfetivada() {
		Cliente cliente = montarObjeto();
		
		Cliente clienteSalvoEntityManager = entityManager.persist(cliente);
		clienteSalvoEntityManager.setNome("Diego");
		
		Cliente clienteAtualizado = repository.save(clienteSalvoEntityManager);
		
		Assertions.assertThat(clienteAtualizado.getId()).isNotNull();
		Assertions.assertThat(clienteAtualizado.getEmail()).isEqualTo(clienteSalvoEntityManager.getEmail());
	}
	
	@Test
	public void verificarExistenciaEmailCadastrado() {
		Cliente cliente = montarObjeto();
		
		entityManager.persist(cliente);
		
		boolean exists = repository.existsByEmail(cliente.getEmail());
		
		Assertions.assertThat(exists).isTrue();
	}
	
	@Test
	public void verificarExistenciaEmailNaoCadastrado() {
		boolean exists = repository.existsByEmail("diego.felix@email.com");
		
		Assertions.assertThat(exists).isFalse();
	}
	
	@Test 
	public void verificarExistenciaCpfCadastrado() {
		Cliente cliente = montarObjeto();
		
		entityManager.persist(cliente);
		
		boolean exists = repository.existsByCpf(cliente.getCpf());
		
		Assertions.assertThat(exists).isTrue();
	}
	
	@Test
	public void verificarExistenciaDeCpfNaoCadastrado() {
		boolean exists = repository.existsByCpf("00000000000");
		
		Assertions.assertThat(exists).isFalse();
	}
	
	public Cliente montarObjeto() {
		return Cliente.builder()
				.nome("Diego Félix")
				.cpf("00000000000")
				.email("diego.felix@email.com")
				.build();
	}

}
