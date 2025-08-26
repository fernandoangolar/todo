package ao.com.angotech.dto.task;

import ao.com.angotech.enums.TaskStatus;


import java.time.LocalDateTime;
import java.util.Date;

public record TaskResponse(

        Long id,
        String title,
        String description,
        Date dueDate,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId
) {
}
