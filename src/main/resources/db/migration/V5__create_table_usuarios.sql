CREATE TABLE tb_usuarios (
     id BINARY(16) NOT NULL,
     password VARCHAR(255) NOT NULL,
     email VARCHAR(255) NOT NULL,
     pessoa_id BINARY(16) NOT NULL,
     role VARCHAR(50) NOT NULL,

     CONSTRAINT pk_tb_usuarios PRIMARY KEY (id),
     CONSTRAINT uk_tb_usuarios_email UNIQUE (email),
     CONSTRAINT uk_tb_usuarios_pessoa UNIQUE (pessoa_id),

     CONSTRAINT fk_tb_usuarios_pessoa
         FOREIGN KEY (pessoa_id)
             REFERENCES tb_pessoas(id)
);
