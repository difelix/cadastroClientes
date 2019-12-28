package com.cadastroClientes.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneDto {

	private Long id;
	private String numero;
	private Long idCliente;
}
