package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.model.Merenda;
import com.example.M.E.N.O.S.service.MerendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/merendas")
public class MerendaController {

    private final MerendaService merendaService;

    public MerendaController(MerendaService merendaService) {
        this.merendaService = merendaService;
    }

    @GetMapping
    public List<Merenda> listar() {
        return merendaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Merenda buscarPorId(@PathVariable Long id) {
        return merendaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Merenda criar(@Valid @RequestBody Merenda merenda) {
        return merendaService.salvar(merenda);
    }

    @PutMapping("/{id}")
    public Merenda atualizar(@PathVariable Long id, @Valid @RequestBody Merenda merenda) {
        return merendaService.atualizar(id, merenda);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        merendaService.deletar(id);
    }

    // Envio da imagem (multipart/form-data, campo "arquivo")
    @PostMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enviarImagem(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) {
        merendaService.salvarImagem(id, arquivo);
    }

    // Download/visualização da imagem
    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> buscarImagem(@PathVariable Long id) {
        Merenda merenda = merendaService.buscarPorId(id);
        if (merenda.getImagem() == null) {
            return ResponseEntity.notFound().build();
        }
        MediaType tipo = merenda.getImagemTipo() != null
                ? MediaType.parseMediaType(merenda.getImagemTipo())
                : MediaType.APPLICATION_OCTET_STREAM;
        return ResponseEntity.ok().contentType(tipo).body(merenda.getImagem());
    }
}
