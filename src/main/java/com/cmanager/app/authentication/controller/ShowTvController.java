package com.cmanager.app.authentication.controller;


import com.cmanager.app.authentication.service.ShowTvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/show")
@Tag(
        name = "ShowTvController",
        description = "API de chamada externa Show TV"
)
public class ShowTvController {

    private final ShowTvService showTvService;

    public ShowTvController(ShowTvService showTvService) {
        this.showTvService = showTvService;
    }

    @GetMapping("obtem-single-search")
    public ResponseEntity<String> obtemSingleSearch(@PathVariable String nome, @PathVariable codPerfil) {
        final var response = showTvService.obtemSingleSearch(nome,codPerfil);
        return ResponseEntity.ok(response);
    }

    @PostMapping("obtem-single-search-for-page")
    public ResponseEntity<Page<DTO>>paginacao(Pageable pageable) {
        final Page<DTO> dto = showTvService.paginacao(pageable);
        return ResponseEntity.ok(dto);
    }
}
