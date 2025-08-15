package com.eduardo.stockify.repositories;

import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.models.Produto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNome(String nome);

    @Modifying
    @Query("UPDATE Produto p SET p.nome = :nome, p.descricao = :descricao, p.preco = :preco, p.quantidade = :quantidade, p.categoria = :categoria WHERE p.id = :id")
    int atualizarProduto(Long id, String nome, String descricao, double preco, int quantidade, Categoria categoria);

    @Query("SELECT p.quantidade FROM Produto p WHERE p.id = :produtoId")
    int verificarQuantidade(Long produtoId);

    @Modifying
    @Query("UPDATE Produto p SET p.quantidade = :quantidadeAtualizada WHERE p.id = :produtoId")
    int alterarQuantidade(Long produtoId, int quantidadeAtualizada);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
       UPDATE Produto p
          SET p.quantidade = p.quantidade + :delta
        WHERE p.id = :produtoId
          AND (p.quantidade + :delta) >= 0
    """)
    int aplicarDeltaDeEstoque(Long produtoId, int delta);
}
