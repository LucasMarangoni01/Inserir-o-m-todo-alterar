package com.pbe.db_sorveteria.controller;


import com.pbe.db_sorveteria.model.Curso;
import com.pbe.db_sorveteria.repository.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    private final String uploadDir = "src/main/resources/static/foto_pessoa/";

    // ==============================
    // LISTAGEM
    // ==============================
    @GetMapping("/curso/listagem")
    public String listagem(Model model) {

        model.addAttribute("cursos", cursoRepository.findAll());

        return "/curso/listagem";
    }

    // ==============================
    // CADASTRO
    // ==============================
    @GetMapping("/curso/cadastro")
    public String cadastro(Model model) {

        model.addAttribute("curso", new Curso());

        return "/curso/cadastro";
    }

    // ==============================
    // ALTERAR
    // ==============================
    @GetMapping("/curso/alterar/{id}")
    public String alterar(@PathVariable Long id, Model model){

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso inválida: " + id));

        model.addAttribute("curso", curso);

        return "/curso/alterar";
    }

    // ==============================
    // SALVAR
    // ==============================
    @PostMapping("/curso/salvar")
    public String salvar(@Valid Curso curso,
                         BindingResult result,
                         @RequestParam(value="arquivoFoto", required=false) MultipartFile arquivoFoto) {

        if (result.hasErrors()) {

            if(curso.getId() != null){
                return "/curso/alterar";
            }

            return "/curso/cadastro";
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

                curso.setFoto(nomeArquivo);
            }
            else if (curso.getId() != null) {

                Curso cursoBanco = cursoRepository.findById(curso.getId()).orElse(null);

                if (cursoBanco != null) {
                    curso.setFoto(cursoBanco.getFoto());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        cursoRepository.save(curso);

        return "redirect:/curso/listagem";
    }


    // ==============================
    // EXCLUIR
    // ==============================
    @GetMapping("/curso/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        cursoRepository.deleteById(id);

        return "redirect:/curso/listagem";
    }
}
