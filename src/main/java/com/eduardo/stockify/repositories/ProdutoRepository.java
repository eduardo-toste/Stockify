package com.eduardo.stockify.repositories;

import com.eduardo.stockify.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
