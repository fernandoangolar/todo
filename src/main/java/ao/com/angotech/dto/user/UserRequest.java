package ao.com.angotech.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "O campo nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 a 100 caracteres")
        String name,

        @NotBlank(message = "O campo e-mail é obrigatório")
        @Email(message = "Formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
        String email,

        @NotBlank(message = "O campo e-mail é obrigatório")
        @Size(min = 6, max = 12, message = "Senha deve ter no mínimo 8 caracteres e no máximo 12")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Senha deve conter pelo menos uma letra maiúscula e um número")
        String password
) {
}
