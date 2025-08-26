package ao.com.angotech.dto.task;

import ao.com.angotech.enums.TaskStatus;

import java.util.Date;

public record TaskFilter(
        TaskStatus status,
        Date dueDate
) {
}
