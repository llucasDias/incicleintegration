package com.lucas.incicleintegration.controller;


import com.lucas.incicleintegration.service.ImportService;
import com.lucas.incicleintegration.service.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
public class FrontController {


    private final ImportService importService;
    private final RegisterService registerService;


    public FrontController(ImportService importService, RegisterService registerService) {
        this.importService = importService;
        this.registerService = registerService;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("matricula", "");
        return "index";
    }

    @PostMapping("/buscar")
    public String buscarColaborador(@RequestParam("matricula") String matricula, Model model) {
        try {
            var colaboradores = importService.buscarColaboradorParaView(matricula);
            model.addAttribute("colaboradores", colaboradores);
            model.addAttribute("matricula", matricula);

            if (colaboradores.isEmpty()) {
                model.addAttribute("mensagem", "Nenhum colaborador encontrado.");
                model.addAttribute("sucesso", false);
            } else {
                model.addAttribute("mensagem", "Colaborador encontrado!");
                model.addAttribute("sucesso", true);
            }
        } catch (Exception e) {
            model.addAttribute("mensagem", e.getMessage());
            model.addAttribute("sucesso", false);
        }
        return "index";
    }

    @PostMapping("/enviar")
    public String enviarConvite(@RequestParam("matricula") String matricula, Model model) {
        try {
            registerService.registrarColaborador(matricula);
            model.addAttribute("mensagem", "Convite e registro enviados com sucesso!");
            model.addAttribute("sucesso", true);
        } catch (Exception e) {
            model.addAttribute("mensagem", e.getMessage());
            model.addAttribute("sucesso", false);
        }
        return "index";
    }

    @PostMapping("/limpar")
    public String limpar(Model model) {
        model.addAttribute("colaboradores", null);
        model.addAttribute("mensagem", null);
        model.addAttribute("matricula", "");
        model.addAttribute("sucesso", false);
        return "index";
    }
}