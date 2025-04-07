package com.eduardo.stockify.models;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.models.enums.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Produto")
@Table(name = "produtos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(name = "dataCadastro")
    private LocalDateTime dataCadastro;

    public Produto(ProdutoRequest data) {
        this.nome = data.nome();
        this.descricao = data.descricao();
        this.preco = data.preco();
        this.quantidade = data.quantidade();
        this.categoria = data.categoria();
    }
}
