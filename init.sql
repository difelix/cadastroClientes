CREATE USER admincadastro with encrypted password 'teste';

CREATE DATABASE cadastroclientes OWNER admincadastro;
GRANT CONNECT ON DATABASE cadastroclientes TO admincadastro;

USE cadastroclientes;

CREATE SCHEMA clientes;

GRANT USAGE ON SCHEMA clientes TO admincadastro;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA clientes TO admincadastro;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA clientes TO admincadastro;

create table clientes.cliente (
    id bigserial PRIMARY KEY,
	nome varchar NOT NULL,
	email varchar NOT NULL UNIQUE,
	cpf varchar(11) NOT NULL UNIQUE
);

create table clientes.endereco (
    id bigserial PRIMARY KEY,
	rua varchar NOT NULL,
	cep varchar(8) NOT NULL,
	bairro varchar NOT NULL,
	cidade varchar NOT NULL,
	estado varchar NOT NULL,
	complemento varchar,
	referencia varchar,
	id_cliente bigint references clientes.cliente (id)
);

create table clientes.telefone (
    id bigserial PRIMARY KEY,
	numero varchar(11) NOT NULL,
	id_cliente bigint references clientes.cliente (id)
);