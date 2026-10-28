CREATE TABLE tb_compras (
    id CHAR(36) NOT NULL,
    valor_usd DECIMAL(15, 2) NOT NULL,
    cotacao_utilizada DECIMAL(15, 6) NOT NULL,
    valor_convertido DECIMAL(15, 2) NOT NULL,
    taxa_servico DECIMAL(15, 2) NOT NULL,
    tributo_simulado DECIMAL(15, 2) NOT NULL,
    valor_final_brl DECIMAL(15, 2) NOT NULL,
    data_cotacao DATE NOT NULL,
    data_compra DATETIME NOT NULL,
    cliente_id CHAR(36) NOT NULL,

    CONSTRAINT pk_tb_compras PRIMARY KEY (id),
    CONSTRAINT fk_tb_compras_cliente
        FOREIGN KEY (cliente_id) REFERENCES tb_clientes(id)
);

CREATE INDEX idx_tb_compras_cliente_data
    ON tb_compras (cliente_id, data_compra);