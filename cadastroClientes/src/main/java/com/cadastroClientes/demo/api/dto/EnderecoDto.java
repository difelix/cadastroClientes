package com.cadastroClientes.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDto {

	private Long id;
	private String rua;
	private String cep;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private String referencia;
	private Long idCliente;
}
