package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.model.Menu;
import com.example.M.E.N.O.S.model.Merenda;
import com.example.M.E.N.O.S.repository.MerendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerendaService {

    private final MerendaRepository merendaRepository;

    // Injeção de dependência via construtor
    public MerendaService(MerendaRepository merendaRepository) {
        this.merendaRepository = merendaRepository;
    }

    public List<Merenda> listarTodos() {
        return merendaRepository.findAll();
    }

    public Merenda buscarPorId(Long id) {
        return merendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Merenda não encontrada com id: " + id));
    }

    public Merenda salvar(Merenda merenda) {
        merenda.setId(null); // garante que o POST sempre cria um registro novo
        return merendaRepository.save(merenda);
    }

    // Atualiza só nome e quantidade; a imagem é alterada pelo endpoint próprio.
    public Merenda atualizar(Long id, Merenda dadosAtualizados) {
        Merenda merenda = buscarPorId(id);
        merenda.setNome(dadosAtualizados.getNome());
        merenda.setQuantidade(dadosAtualizados.getQuantidade());
        return merendaRepository.save(merenda);
    }

    @Transactional
    public void deletar(Long id) {
        Merenda merenda = buscarPorId(id);
        // Menu é o lado dono da relação N:N, então tiramos a merenda de cada
        // cardápio antes de excluir (senão o banco recusa por chave estrangeira).
        for (Menu menu : new ArrayList<>(merenda.getMenus())) {
            menu.getMerendas().remove(merenda);
        }
        merendaRepository.delete(merenda);
    }

    @Transactional
    public void salvarImagem(Long id, MultipartFile arquivo) {
        Merenda merenda = buscarPorId(id);

        if (arquivo == null || arquivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhuma imagem foi enviada");
        }
        String tipo = arquivo.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O arquivo enviado não é uma imagem");
        }

        try {
            merenda.setImagem(arquivo.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Não foi possível ler a imagem enviada");
        }
        merenda.setImagemTipo(tipo);
        merendaRepository.save(merenda);
    }
}
