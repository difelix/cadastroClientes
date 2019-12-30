package com.cadastroClientes.demo.api.resource;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cadastroClientes.demo.api.dto.TelefoneDto;
import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.model.entity.Telefone;
import com.cadastroClientes.demo.service.ClienteService;
import com.cadastroClientes.demo.service.TelefoneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = TelefoneResource.class)
@AutoConfigureMockMvc
public class TelefoneResourceTest {
	
	static final String API_ADRESS = "/api/telefones";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	TelefoneService telefoneService;
	
	@MockBean
	ClienteService clienteService;
	
	@Test
	public void deveCadastrarTelefoneComSucesso() throws JsonProcessingException, Exception {
		String numero = "80000000000";
		
		TelefoneDto dto = TelefoneDto.builder().numero(numero).idCliente(1L).build();
		Cliente cliente = Cliente.builder().cpf("11111111111").email("teste@email.com").nome("Teste").id(1L).build();
		Telefone telefone = Telefone.builder().id(1L).numero(numero).cliente(cliente).build();
		
		Mockito.when(telefoneService.cadastrarTelefone(Mockito.any(Telefone.class))).thenReturn(telefone);
		
		Optional<Cliente> clienteOptional = Optional.of(cliente);
		Mockito.when(clienteService.buscarClientePorId(Mockito.anyLong())).thenReturn(clienteOptional);
		String json = new ObjectMapper().writeValueAsString(dto);
		
	    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	    		.post(API_ADRESS)
	    		.accept(MediaType.APPLICATION_JSON)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(json);
		
	    mvc.perform(request)
	       .andExpect(MockMvcResultMatchers.status().isCreated())
	       .andExpect(MockMvcResultMatchers.jsonPath("id").value(telefone.getId()))
	       .andExpect(MockMvcResultMatchers.jsonPath("numero").value(telefone.getNumero()))
	       .andExpect(MockMvcResultMatchers.jsonPath("cliente").value(telefone.getCliente()));	
	}

}
