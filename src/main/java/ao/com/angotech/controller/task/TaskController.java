package ao.com.angotech.controller.task;

import ao.com.angotech.dto.task.TaskFilter;
import ao.com.angotech.dto.task.TaskRequest;
import ao.com.angotech.dto.task.TaskResponse;
import ao.com.angotech.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskResponse> save(@Valid @RequestBody TaskRequest request) throws AccessDeniedException {
        TaskResponse response = taskService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

//    @GetMapping
//    public ResponseEntity<List<TaskResponse>> list(
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) Date dueDate
//    ) throws AccessDeniedException {
//        return ResponseEntity.ok(taskService.findAllByCurrentUser());
//    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<TaskResponse>> getAll() throws AccessDeniedException {
        List<TaskResponse> tasks = taskService.findAllByCurrentUser();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) throws AccessDeniedException {
        TaskResponse task = taskService.findByIdAndCurrentUser(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) throws AccessDeniedException {
        TaskResponse response = taskService.updateByCurrentUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws AccessDeniedException {
        taskService.deleteByCurrentUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> markAsCompleted(@PathVariable Long id) throws AccessDeniedException {
        return ResponseEntity.ok(taskService.completeTask(id));
    }


    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> overdue() throws AccessDeniedException {
        return ResponseEntity.ok(taskService.findOverdueTasks());
    }
}
