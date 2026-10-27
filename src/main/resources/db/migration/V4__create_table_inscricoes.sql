CREATE TABLE tb_inscricoes (
   id BINARY(16) NOT NULL,
   pessoa_id BINARY(16) NOT NULL,
   evento_id BINARY(16) NOT NULL,
   status_inscricao VARCHAR(20) NOT NULL,
   data_hora_inscricao DATETIME NOT NULL,

   PRIMARY KEY (id),

   CONSTRAINT uk_inscricao_pessoa_evento
       UNIQUE (pessoa_id, evento_id),

   CONSTRAINT fk_inscricao_pessoa
       FOREIGN KEY (pessoa_id)
           REFERENCES tb_pessoas (id),

   CONSTRAINT fk_inscricao_evento
       FOREIGN KEY (evento_id)
           REFERENCES tb_eventos (id)
);
