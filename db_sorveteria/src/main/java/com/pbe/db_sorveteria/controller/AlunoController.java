package com.pbe.db_sorveteria.controller;

import com.pbe.db_sorveteria.model.Aluno;
import com.pbe.db_sorveteria.repository.AlunoRepository;
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
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    private final String uploadDir = "src/main/resources/static/foto_pessoa/";

    // LISTAGEM
    @GetMapping("/aluno/listagem")
    public String listagem(Model model){

        model.addAttribute("alunos", alunoRepository.findAll());

        return "/aluno/listagem";
    }

    // CADASTRO
    @GetMapping("/aluno/cadastro")
    public String cadastro(Model model){

        model.addAttribute("aluno", new Aluno());

        return "/aluno/cadastro";
    }

    // ALTERAR
    @GetMapping("/aluno/alterar/{id}")
    public String alterar(@PathVariable Long id, Model model){

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno inválido"));

        model.addAttribute("aluno", aluno);

        return "/aluno/alterar";
    }

    // SALVAR
    @PostMapping("/aluno/salvar")
    public String salvar(@Valid Aluno aluno,
                         BindingResult result,
                         @RequestParam(value="arquivoFoto", required=false) MultipartFile arquivoFoto){

        if(result.hasErrors()){

            if(aluno.getId() != null){
                return "/aluno/alterar";
            }

            return "/aluno/cadastro";
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

                aluno.setFoto(nomeArquivo);
            }
            else if(aluno.getId() != null){

                Aluno alunoBanco = alunoRepository.findById(aluno.getId()).orElse(null);

                if(alunoBanco != null){
                    aluno.setFoto(alunoBanco.getFoto());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        alunoRepository.save(aluno);

        return "redirect:/aluno/listagem";
    }

    // EXCLUIR
    @GetMapping("/aluno/excluir/{id}")
    public String excluir(@PathVariable Long id){

        alunoRepository.deleteById(id);

        return "redirect:/aluno/listagem";
    }
}
