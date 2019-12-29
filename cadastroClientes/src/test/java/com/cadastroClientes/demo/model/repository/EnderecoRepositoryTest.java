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
import com.cadastroClientes.demo.model.entity.Endereco;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EnderecoRepositoryTest {
	
	@Autowired
	EnderecoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void cadastrarEnderecoComSucesso() {
		Cliente cliente = criarCliente();
		cliente = entityManager.persist(cliente);
		
		Endereco endereco = criarEndereco();
		endereco.setCliente(cliente);
		
		Endereco enderecoSalvo = repository.save(endereco);
		
		Assertions.assertThat(enderecoSalvo.getId()).isNotNull();	
	}
	
	@Test
	public void atualizarEnderecoComSucesso() {
		Cliente cliente = criarCliente();
		cliente = entityManager.persist(cliente);
		
		Endereco endereco = criarEndereco();
		endereco.setCliente(cliente);
		endereco = entityManager.persist(endereco);
		
		endereco.setCidade("Osasco");
		Endereco enderecoAtualizado = repository.save(endereco);
		
		Assertions.assertThat(enderecoAtualizado.getId()).isNotNull();
		Assertions.assertThat(enderecoAtualizado.getCidade()).isEqualTo(endereco.getCidade());
	}
	
	public Cliente criarCliente() {
		return Cliente.builder()
				.nome("Teste")
				.cpf("11111111111")
				.email("teste@email.com")
				.build();
	}
	
	public Endereco criarEndereco() {
		return Endereco.builder()
				.rua("Rua 15")
				.cidade("São Paulo")
				.estado("SP")
				.bairro("Bairro")
				.cep("11111111")
				.build();
	}

}
