package ao.com.angotech.dto.task;

import ao.com.angotech.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

public record TaskRequest (

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String title,

        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
        String description,

        @FutureOrPresent(message = "A data de vencimento deve ser hoje ou no futuro")
        Date dueDate,

        TaskStatus status
) {
}
