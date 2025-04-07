CREATE TABLE movimentacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    quantidade INT NOT NULL,
    data DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_movimentacao_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);
