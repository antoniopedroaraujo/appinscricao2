CREATE TABLE tb_clientes (
                             id CHAR(36) NOT NULL,
                             nome VARCHAR(100) NOT NULL,
                             email VARCHAR(150) NOT NULL,

                             CONSTRAINT pk_tb_clientes PRIMARY KEY (id),
                             CONSTRAINT uk_tb_clientes_email UNIQUE (email)
);
