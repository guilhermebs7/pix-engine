package engine.pix_core.controller;

import engine.pix_core.dto.Response.ComprovanteResponse;
import engine.pix_core.service.ComprovanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comprovantes")
@Tag(name="Comprovantes", description = "Endpoints para consulta e emissão de comprovantes Pix")
public class ComprovanteController {

    @Autowired
    private ComprovanteService comprovanteService;

    @Operation(summary = "Buscar comprovante pelo Id")
    @GetMapping("/{id}")
    public ResponseEntity<ComprovanteResponse> buscarPorId(@PathVariable Long id){
        ComprovanteResponse response= comprovanteService.buscarComprovantePorId(id);
        return ResponseEntity.ok(response);
    }


}
