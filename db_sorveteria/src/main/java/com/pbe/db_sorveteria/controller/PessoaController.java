package com.pbe.db_sorveteria.controller;

import com.pbe.db_sorveteria.model.Pessoa;
import com.pbe.db_sorveteria.repository.PessoaRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    private final String uploadDir = "src/main/resources/static/foto_pessoa/";

    // ==============================
    // LISTAGEM
    // ==============================
    @GetMapping("/pessoa/listagem")
    public String listagem(Model model) {

        model.addAttribute("pessoas", pessoaRepository.findAll());

        return "/pessoa/listagem";
    }

    // ==============================
    // CADASTRO
    // ==============================
    @GetMapping("/pessoa/cadastro")
    public String cadastro(Model model) {

        model.addAttribute("pessoa", new Pessoa());

        return "/pessoa/cadastro";
    }

    // ==============================
    // ALTERAR
    // ==============================
    @GetMapping("/pessoa/alterar/{id}")
    public String alterar(@PathVariable Long id, Model model){

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa inválida: " + id));

        model.addAttribute("pessoa", pessoa);

        return "/pessoa/alterar";
    }

    // ==============================
    // SALVAR
    // ==============================
    @PostMapping("/pessoa/salvar")
    public String salvar(@Valid Pessoa pessoa,
                         BindingResult result,
                         @RequestParam(value="arquivoFoto", required=false) MultipartFile arquivoFoto) {

        if (result.hasErrors()) {

            if(pessoa.getId() != null){
                return "/pessoa/alterar";
            }

            return "/pessoa/cadastro";
        }

        try {

            if (arquivoFoto != null && !arquivoFoto.isEmpty()) {

                String nomeArquivo = UUID.randomUUID() + "_" + arquivoFoto.getOriginalFilename();

                Path caminhoDiretorio = Paths.get(uploadDir).toAbsolutePath();

                if (!Files.exists(caminhoDiretorio)) {
                    Files.createDirectories(caminhoDiretorio);
                }

                Path caminhoArquivo = caminhoDiretorio.resolve(nomeArquivo);

                arquivoFoto.transferTo(caminhoArquivo.toFile());

                pessoa.setFoto(nomeArquivo);
            }
            else if (pessoa.getId() != null) {

                Pessoa pessoaBanco = pessoaRepository.findById(pessoa.getId()).orElse(null);

                if (pessoaBanco != null) {
                    pessoa.setFoto(pessoaBanco.getFoto());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        pessoaRepository.save(pessoa);

        return "redirect:/pessoa/listagem";
    }


    // ==============================
    // EXCLUIR
    // ==============================
    @GetMapping("/pessoa/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        pessoaRepository.deleteById(id);

        return "redirect:/pessoa/listagem";
    }
}

