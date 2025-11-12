package com.lucas.incicleintegration.controller;


import com.lucas.incicleintegration.dto.invite.InviteResponseWrapper;
import com.lucas.incicleintegration.service.ImportService;
import com.lucas.incicleintegration.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/invite")

public class FrontController {



    private final ImportService importService;
    private final RegisterService registerService;


    public FrontController(ImportService importService, RegisterService registerService) {
        this.importService = importService;
        this.registerService = registerService;
    }

    /**
     * Endpoint simples para importar e registrar colaborador via matrícula
     * Ex.: GET /api/invite?matricula=001205
     */

    @GetMapping

    public ResponseEntity<String> importarERegistrar(@RequestParam String matricula) {


        importService.importarColaboradores(matricula);

        registerService.registrarColaborador(matricula);

        return ResponseEntity.ok("Colaborador processado com sucesso: " + matricula);
    }
}
