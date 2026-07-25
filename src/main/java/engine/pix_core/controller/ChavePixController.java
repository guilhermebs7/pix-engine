package engine.pix_core.controller;

import engine.pix_core.dto.Request.ChavePixRequest;
import engine.pix_core.dto.Response.ChavePixResponse;
import engine.pix_core.service.ChavePixService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chaves-pix")
@Tag(name = "Chave PIX",description = "Endpoint para cadastrar chave PIX")
public class ChavePixController {

    @Autowired
    private ChavePixService chavePixService;

    @PostMapping
    public ResponseEntity<ChavePixResponse> cadastrarChave(@RequestBody @Valid ChavePixRequest request){
        ChavePixResponse response= chavePixService.cadastrarChave(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
