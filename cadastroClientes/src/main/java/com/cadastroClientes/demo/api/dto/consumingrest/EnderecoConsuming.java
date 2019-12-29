package com.cadastroClientes.demo.api.dto.consumingrest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoConsuming {

	private String cep;
	private String logradouro;
	private String bairro;
	private String localidade;
	private String uf;
	private boolean erro;
}
