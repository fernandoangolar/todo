package ao.com.angotech.service;

import ao.com.angotech.dto.task.TaskFilter;
import ao.com.angotech.dto.task.TaskRequest;
import ao.com.angotech.dto.task.TaskResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface TaskService {

    TaskResponse save(TaskRequest request) throws AccessDeniedException;
    List<TaskResponse> findAllByCurrentUser() throws AccessDeniedException;
    List<TaskResponse> findAllByCurrentUser(TaskFilter filter) throws AccessDeniedException;
    TaskResponse findByIdAndCurrentUser(Long id) throws AccessDeniedException;
    TaskResponse updateByCurrentUser(Long id, TaskRequest request) throws AccessDeniedException;
    void deleteByCurrentUser(Long id) throws AccessDeniedException;
    TaskResponse completeTask(Long id) throws AccessDeniedException;

    List<TaskResponse> findOverdueTasks() throws AccessDeniedException;
}
