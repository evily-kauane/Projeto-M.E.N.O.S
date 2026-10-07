package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
