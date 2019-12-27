package com.cadastroClientes.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endereco", schema = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "rua")
	private String rua;
	
	@Column(name = "cep")
	private String cep;
	
	@Column(name = "bairro")
	private String bairro;
	
	@Column(name = "cidade")
	private String cidade;
	
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "complemento")
	private String complemento;
	
	@Column(name = "referencia")
	private String referencia;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
}
