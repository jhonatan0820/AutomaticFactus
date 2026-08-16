package com.automaticfactus.dtos;

import com.automaticfactus.entities.ClientEntity;

public record ClientDto(
        boolean found,
        Integer id,
        String nombre,
        Long telefono,
        String direccion,
        String correo
) {
    public static ClientDto from(ClientEntity c) {
        return new ClientDto(true, c.getIdClient(), c.getName(),
                c.getCellphone(), c.getAddress(), c.getEmail());
    }
}