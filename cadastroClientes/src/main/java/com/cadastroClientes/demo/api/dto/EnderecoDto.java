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
	private String cep;
	private Long idCliente;
	private String complemento;
	private String referencia;
}
