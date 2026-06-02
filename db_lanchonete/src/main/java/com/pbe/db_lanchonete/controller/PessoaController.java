package com.pbe.db_lanchonete.controller;

import com.pbe.db_lanchonete.model.Pessoa;
import com.pbe.db_lanchonete.repository.PessoaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    // ==============================
    // Página Inicial  (localhost:8080/)
    // ==============================
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ==============================
    // LISTAGEM ( /pessoa/listagem )
    // ==============================
    @GetMapping("/pessoa/listagem")
    public String listagem(Model model) {
        model.addAttribute("pessoas", pessoaRepository.findAll());
        return "listagem";
    }

    // ==============================
    // CADASTRO ( /pessoa/cadastro )
    // ==============================
    @GetMapping("/pessoa/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("pessoa", new Pessoa());
        return "cadastro";
    }

    // ==============================
    // SALVAR (POST /pessoa/salvar)
    // ==============================
    @PostMapping("/pessoa/salvar")
    public String salvar(@Valid Pessoa pessoa, BindingResult result) {

        if (result.hasErrors()) {
            return "cadastro";
        }

        pessoaRepository.save(pessoa);
        return "redirect:/pessoa/listagem";
    }

    // ==============================
    // EXCLUIR ( /pessoa/excluir/{id} )
    // ==============================
    @GetMapping("/pessoa/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        pessoaRepository.deleteById(id);
        return "redirect:/pessoa/listagem";
    }
}

