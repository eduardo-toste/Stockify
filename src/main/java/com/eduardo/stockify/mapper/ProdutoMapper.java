package com.eduardo.stockify.mapper;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.dtos.ProdutoUpdateResponse;
import com.eduardo.stockify.models.Produto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class ProdutoMapper {

    public static Produto toEntity(ProdutoRequest dados) {
        return new Produto(
                null,
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.quantidade(),
                dados.categoria(),
                LocalDateTime.now()
        );
    }

    public static Produto toEntity(Long id, ProdutoRequest dados) {
        return new Produto(
                id,
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.quantidade(),
                dados.categoria()
        );
    }

    public static ProdutoResponse toDTO(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getCategoria(),
                produto.getDataCadastro()
        );
    }

    public static ProdutoUpdateResponse toUpdateDTO(Produto produto) {
        return new ProdutoUpdateResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getCategoria()
        );
    }

    public static Page<ProdutoResponse> toPageDTO(Page<Produto> page) {
        return page.map(ProdutoMapper::toDTO);
    }

}
