package com.automaticfactus.controller;

import com.automaticfactus.service.TypeIdentificationService;
import com.automaticfactus.service.TypeIdentificationService.TipoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TypeIdentificationController {

    private final TypeIdentificationService service;

    public TypeIdentificationController(TypeIdentificationService service) {
        this.service = service;
    }

    @GetMapping("/api/tipos-identificacion")
    public List<TipoDto> list() {
        return service.listActive();
    }
}
