package com.eduardo.stockify.repositories;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProdutoRepository produtoRepository;

    @Test
    @DisplayName("Encontra um produto existente com o nome")
    void existsByNomeCase1(){
        var nome = "Mouse Gamer RGB";
        var produto = new ProdutoRequest(
                nome,
                "Mouse com 7 botões programáveis, iluminação RGB e sensor óptico de alta precisão.",
                149.90,
                50 ,
                Categoria.ELETRONICO
        );
        this.criarProduto(produto);

        var produtoEcontrando = this.produtoRepository.existsByNome(nome);

        assertThat(produtoEcontrando).isTrue();
    }

    @Test
    @DisplayName("Não encontra um produto existente com o nome")
    void existsByNomeCase2(){
        var nome = "Mouse Gamer RGB";
        var produtoEcontrando = this.produtoRepository.existsByNome(nome);

        assertThat(produtoEcontrando).isFalse();
    }

    private Produto criarProduto(ProdutoRequest data){
        var novoProduto = new Produto(data);
        this.entityManager.persist(novoProduto);

        return novoProduto;
    }

}