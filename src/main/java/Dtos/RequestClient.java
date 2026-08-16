package Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequestClient(

        @NotBlank
        String Name,

        @NotNull
        @Positive
        Integer IdTypeIdentification,

        @NotBlank
        @Positive
        String IdentificationNumber,

        @NotBlank
        @Positive
        String Cellphone,

        @NotBlank
        String Address,

        @Email
        String email

) {}
