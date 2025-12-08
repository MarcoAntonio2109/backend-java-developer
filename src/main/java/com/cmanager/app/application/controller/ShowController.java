package com.cmanager.app.application.controller;
import com.cmanager.app.application.data.ShowDto;
import com.cmanager.app.application.service.ShowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shows")
@Tag(
        name = "ShowController",
        description = "API de controle autenticações"
)
public class ShowController {
    @Autowired
    private ShowService showService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> saveEpisodes(@RequestParam String name) {
        return ResponseEntity.ok(showService.saveShow(name));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowDto>getShow(@RequestParam String name) {
        return ResponseEntity.ok(showService.findShow(name));
    }

}
