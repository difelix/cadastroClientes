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
		Cliente cliente = Cliente.builder()
				.cpf("00000000000")
				.email("diego@email.com")
				.nome("Diego")
				.build();
		cliente = entityManager.persist(cliente);
		
		Telefone telefone = Telefone.builder()
				.numero("19111112222")
				.cliente(cliente)
				.build();
		
		Telefone telefoneSalvo = repository.save(telefone);
		
		Assertions.assertThat(telefoneSalvo.getId()).isNotNull();
	}
	
	@Test
	public void atualizarTelefoneComSucesso() {
		Cliente cliente = Cliente.builder()
				.cpf("00000000000")
				.email("diego@email.com")
				.nome("Diego")
				.build();
		cliente = entityManager.persist(cliente);
		
		Telefone telefone = Telefone.builder()
				.numero("19111112222")
				.cliente(cliente)
				.build();
		Telefone telefoneSalvoEM = entityManager.persist(telefone);
		telefoneSalvoEM.setNumero("21111112222");
		
		Telefone telefoneAtualizado = repository.save(telefoneSalvoEM);
		
		Assertions.assertThat(telefoneAtualizado.getNumero()).isEqualTo(telefoneSalvoEM.getNumero());	
	}

}
