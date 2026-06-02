package com.pbe.db_sorveteria.controller;

import com.pbe.db_sorveteria.model.Funcionario;
import com.pbe.db_sorveteria.repository.FuncionarioRepository;
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
public class FuncionarioController {


    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private final String uploadDir = "src/main/resources/static/foto_pessoa/";

    // LISTAGEM
    @GetMapping("/funcionario/listagem")
    public String listagem(Model model){

        model.addAttribute("funcionarios", funcionarioRepository.findAll());

        return "/funcionario/listagem";
    }

    // CADASTRO
    @GetMapping("/funcionario/cadastro")
    public String cadastro(Model model){

        model.addAttribute("funcionario", new Funcionario());

        return "/funcionario/cadastro";
    }

    // ALTERAR
    @GetMapping("/funcionario/alterar/{id}")
    public String alterar(@PathVariable Long id, Model model){

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("funcionario inválido"));

        model.addAttribute("funcionario", funcionario);

        return "/funcionario/alterar";
    }

    // SALVAR
    @PostMapping("/funcionario/salvar")
    public String salvar(@Valid Funcionario funcionario,
                         BindingResult result,
                         @RequestParam(value="arquivoFoto", required=false) MultipartFile arquivoFoto){

        if(result.hasErrors()){

            if(funcionario.getId() != null){
                return "/funcionario/alterar";
            }

            return "/funcionario/cadastro";
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

                funcionario.setFoto(nomeArquivo);
            }
            else if(funcionario.getId() != null){

                Funcionario funcionarioBanco = funcionarioRepository.findById(funcionario.getId()).orElse(null);

                if(funcionarioBanco != null){
                    funcionario.setFoto(funcionarioBanco.getFoto());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        funcionarioRepository.save(funcionario);

        return "redirect:/funcionario/listagem";
    }

    // EXCLUIR
    @GetMapping("/funcionario/excluir/{id}")
    public String excluir(@PathVariable Long id){

        funcionarioRepository.deleteById(id);

        return "redirect:/funcionario/listagem";
    }


}

