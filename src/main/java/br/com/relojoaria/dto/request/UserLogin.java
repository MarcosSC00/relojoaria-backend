package br.com.relojoaria.dto.request;

import br.com.relojoaria.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLogin {
    @NotBlank
    @Size(max = 200, min=3)
    private String name;

    @NotBlank
    @Size(max = 200, min=3)
    private String password;
}
