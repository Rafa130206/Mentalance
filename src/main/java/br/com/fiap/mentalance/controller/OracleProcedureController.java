package br.com.fiap.mentalance.controller;

import br.com.fiap.mentalance.service.OracleProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oracle")
@RequiredArgsConstructor
public class OracleProcedureController {

    private final OracleProcedureService oracleProcedureService;

    /**
     * Endpoint para exportar dataset completo em JSON usando a procedure Oracle
     */
    @GetMapping(value = "/exportar-json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> exportarDatasetJson() {
        try {
            String json = oracleProcedureService.exportarDatasetJson();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Endpoint para obter dataset completo em JSON usando a procedure pkg_exportacao_json.pr_exportar_dataset_json
     */
    @GetMapping(value = "/dataset-json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> obterDatasetCompletoJson() {
        try {
            String json = oracleProcedureService.exportarDatasetJson();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}

