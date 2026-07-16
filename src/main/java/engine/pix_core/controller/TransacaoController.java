package engine.pix_core.controller;

import engine.pix_core.dto.Request.TransacaoRequest;
import engine.pix_core.dto.Response.TransacaoResponse;
import engine.pix_core.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencias")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<TransacaoResponse> realizarTransferencia(@RequestBody @Valid TransacaoRequest request){
        TransacaoResponse response= transacaoService.processarTransferencia(request);
        return ResponseEntity.ok(response);
    }
}
