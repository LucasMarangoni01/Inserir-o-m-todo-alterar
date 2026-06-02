package com.pbe.db_sorveteria.controller;

import com.pbe.db_sorveteria.model.Professor;
import com.pbe.db_sorveteria.repository.ProfessorRepository;
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
public class ProfessorController {


    @Autowired
    private ProfessorRepository professorRepository;

    private final String uploadDir = "src/main/resources/static/foto_pessoa/";

    // LISTAGEM
    @GetMapping("/professor/listagem")
    public String listagem(Model model){

        model.addAttribute("professores", professorRepository.findAll());

        return "/professor/listagem";
    }

    // CADASTRO
    @GetMapping("/professor/cadastro")
    public String cadastro(Model model){

        model.addAttribute("professor", new Professor());

        return "/professor/cadastro";
    }

    // ALTERAR
    @GetMapping("/professor/alterar/{id}")
    public String alterar(@PathVariable Long id, Model model){

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professor inválido"));

        model.addAttribute("professor", professor);

        return "/professor/alterar";
    }

    // SALVAR
    @PostMapping("/professor/salvar")
    public String salvar(@Valid Professor professor,
                         BindingResult result,
                         @RequestParam(value="arquivoFoto", required=false) MultipartFile arquivoFoto){

        if(result.hasErrors()){

            if(professor.getId() != null){
                return "/professor/alterar";
            }

            return "/professor/cadastro";
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

                professor.setFoto(nomeArquivo);
            }
            else if(professor.getId() != null){

                Professor professorBanco = professorRepository.findById(professor.getId()).orElse(null);

                if(professorBanco != null){
                    professor.setFoto(professorBanco.getFoto());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        professorRepository.save(professor);

        return "redirect:/professor/listagem";
    }

    // EXCLUIR
    @GetMapping("/professor/excluir/{id}")
    public String excluir(@PathVariable Long id){

        professorRepository.deleteById(id);

        return "redirect:/professor/listagem";
    }


}

