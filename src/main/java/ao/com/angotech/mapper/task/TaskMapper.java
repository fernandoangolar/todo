package ao.com.angotech.mapper.task;

import ao.com.angotech.dto.task.TaskRequest;
import ao.com.angotech.dto.task.TaskResponse;
import ao.com.angotech.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest request) {

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());

        if (request.status() != null) {
            task.setStatus(request.status());
        }

        return task;
    }

    public TaskResponse toResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getUser().getId()
        );
    }
}
