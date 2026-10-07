package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Merenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da merenda é obrigatório")
    private String nome;

    // A imagem não vai no JSON (ficaria pesada nas listagens);
    // ela é enviada/baixada pelos endpoints /merendas/{id}/imagem.
    @JsonIgnore
    @Lob
    private byte[] imagem;

    @JsonIgnore
    private String imagemTipo;

    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private int quantidade;

    // @JsonIgnore evita loop infinito no JSON (Merenda -> Menu -> Merenda -> ...)
    @JsonIgnore
    @ManyToMany(mappedBy = "merendas")
    private List<Menu> menus = new ArrayList<>();

    public Merenda(){
    }

    public Merenda(Long id, String nome, byte[] imagem, int quantidade, List<Menu> menus) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.quantidade = quantidade;
        this.menus = menus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getImagemTipo() {
        return imagemTipo;
    }

    public void setImagemTipo(String imagemTipo) {
        this.imagemTipo = imagemTipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    // toString sem "menus" e sem "imagem" para não gerar StackOverflowError
    // (Menu também imprime as merendas) nem imprimir o array de bytes inteiro.
    @Override
    public String toString() {
        return "Merenda{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                '}';
    }
}
