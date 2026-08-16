package com.automaticfactus.controller;

import com.automaticfactus.service.TypeOrderService;
import com.automaticfactus.service.TypeOrderService.TipoOrdenDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TypeOrderController {

    private final TypeOrderService service;

    public TypeOrderController(TypeOrderService service) {
        this.service = service;
    }

    @GetMapping("/api/tipos-orden")
    public List<TipoOrdenDto> list() {
        return service.listActive();
    }
}
