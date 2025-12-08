package com.cmanager.app.application.controller;

import com.cmanager.app.application.data.EpisodesPageResponse;
import com.cmanager.app.application.service.EpisodesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/episodes")
@Tag(
        name = "EpisodesController",
        description = "API de Episodios"
)
public class EpisodesController {
    @Autowired
    private EpisodesService service;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> saveEpisodes(@RequestParam String name) {
        return ResponseEntity.ok(service.saveEpisodes(name));
    }
    @GetMapping
    public ResponseEntity<EpisodesPageResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer season,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer runtime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(service.searchEpisodes(name, season, type, runtime, page, size));
    }

}
