package com.cadastroClientes.demo.api.resource;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cadastroClientes.demo.api.dto.ClienteDto;
import com.cadastroClientes.demo.model.entity.Cliente;
import com.cadastroClientes.demo.service.ClienteService;
import com.cadastroClientes.demo.service.exception.RegraNegocioException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ClienteResource.class)
@AutoConfigureMockMvc
public class ClienteResourceTest {
	
	static final String API_ADRESS = "/api/clientes";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	ClienteService clienteService;
	
	@Test
	public void deveRetornarPositivoParaEmailQueExiste() throws Exception {
		String email = "teste@email.com";
		
		Mockito.when(clienteService.verificarEmail(Mockito.anyString())).thenReturn(true);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API_ADRESS.concat("/checkemail"))
				.param("email", email);				
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isFound());	
	}
	
	@Test
	public void deveRetornarNegativoParaEmailQueNaoExiste() throws Exception {
		String email = "inexistente@email.com";
		
        Mockito.when(clienteService.verificarEmail(Mockito.anyString())).thenReturn(false);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API_ADRESS.concat("/checkemail"))
				.param("email", email);				
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void deveCadastrarUmClienteComSucesso() throws JsonProcessingException, Exception {
		String email = "teste@email.com";
		String cpf = "11111111111";
		
		ClienteDto dto = ClienteDto.builder().cpf(cpf).email(email).build();
		Cliente cliente = Cliente.builder().id(1L).cpf(cpf).email(email).build();
		
		Mockito.when(clienteService.cadastrarCliente(Mockito.any(Cliente.class))).thenReturn(cliente);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API_ADRESS)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isCreated())
		   .andExpect(MockMvcResultMatchers.jsonPath("id").value(cliente.getId()))
		   .andExpect(MockMvcResultMatchers.jsonPath("email").value(cliente.getEmail()))
		   .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(cliente.getCpf()));	
	}
	
	@Test
	public void deveLancarBadRequestAoTentarCadastrarErro() throws JsonProcessingException, Exception {
		String email = "teste@email.com";
		String cpf = "11111111111";
		
		ClienteDto dto = ClienteDto.builder().cpf(cpf).email(email).build();
		
		Mockito.when(clienteService.cadastrarCliente(Mockito.any(Cliente.class))).thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API_ADRESS)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isBadRequest());	
	}
	
	@Test
	public void deveRetornaListaDeClientesEncontrados() throws Exception {
		String email = "teste1@email.com";
		
		Cliente cliente = Cliente.builder().cpf("11111111111").email(email).build();
		List<Cliente> clientesFiltrados = new ArrayList();
		clientesFiltrados.add(cliente);
		
		Mockito.when(clienteService.buscarCliente(Mockito.any(Cliente.class))).thenReturn(clientesFiltrados);
		String json = new ObjectMapper().writeValueAsString(clientesFiltrados);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(API_ADRESS.concat("/searchemail"))
				.param("email", email)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void deveAtualizarClienteComSucesso() throws Exception {
		String email = "teste@email.com";
		String cpf = "11111111111";
		Long id = 1L;
		
		ClienteDto dto = ClienteDto.builder().cpf(cpf).email(email).build();
		Cliente cliente = Cliente.builder().id(id).cpf(cpf).email(email).build();
		Optional<Cliente> clienteOptional = Optional.of(cliente);
		
		Mockito.when(clienteService.buscarClientePorId(Mockito.anyLong())).thenReturn(clienteOptional);
		Mockito.when(clienteService.atualizarCliente(Mockito.any(Cliente.class))).thenReturn(cliente);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(API_ADRESS.concat("/").concat(id.toString()))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void deveAcusarBadRequestAoBuscarClienteNaoCadastrado() throws Exception {
		String email = "teste@email.com";
		String cpf = "11111111111";
		Long id = 1L;
		
		ClienteDto dto = ClienteDto.builder().cpf(cpf).email(email).build();
		
		Mockito.when(clienteService.buscarClientePorId(Mockito.anyLong())).thenReturn(Optional.empty());		
        String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(API_ADRESS.concat("/").concat(id.toString()))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

}
