package ao.com.angotech.service.impl;

import ao.com.angotech.config.SecurityUtils;
import ao.com.angotech.dto.task.TaskFilter;
import ao.com.angotech.dto.task.TaskRequest;
import ao.com.angotech.dto.task.TaskResponse;
import ao.com.angotech.entity.Task;
import ao.com.angotech.entity.User;
import ao.com.angotech.enums.TaskStatus;
import ao.com.angotech.exception.ResourceNotFoundException;
import ao.com.angotech.mapper.task.TaskMapper;
import ao.com.angotech.repository.TaskRepository;
import ao.com.angotech.service.TaskService;
import ao.com.angotech.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }

    @Override
    public TaskResponse save(TaskRequest request) throws AccessDeniedException {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        if ( currentUserId == null ) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        User user = userService.findEntityById(currentUserId);

        Task entity = taskMapper.toEntity(request);
        entity.setUser(user);

        if (entity.getStatus() == null) {
            entity.setStatus(TaskStatus.PENDING);
        }

        Task saveEntity = taskRepository.save(entity);
        return taskMapper.toResponse(saveEntity);
    }

    @Override
    public List<TaskResponse> findAllByCurrentUser() throws AccessDeniedException {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        if ( currentUserId == null ) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        List<Task> tasks = taskRepository.findByUserId(currentUserId);
        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> findAllByCurrentUser(TaskFilter filter) throws AccessDeniedException {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        List<Task> tasks;

        if (filter.status() != null ) {
            tasks = taskRepository.findByUserIdAndStatus(currentUserId, filter.status());
        } else if (filter.dueDate() != null){
            tasks = taskRepository.findByUserIdAndDueDate(currentUserId, filter.dueDate());
        } else {
            tasks = taskRepository.findByUserId(currentUserId);
        }

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public TaskResponse findByIdAndCurrentUser(Long id) throws AccessDeniedException {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        if ( currentUserId == null ) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        Task task = taskRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow( () -> new ResourceNotFoundException(
                        String.format("Tarefa com id %d não encontrada ou não pertence ao usuário", id)
                ));
        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse updateByCurrentUser(Long id, TaskRequest request) throws AccessDeniedException {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        Task existingTask = taskRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow( () -> new ResourceNotFoundException(
                        String.format("Tarefa com id %d não encontrada ou não pertence ao usuário", id)
                ));

        existingTask.setTitle(request.title());
        existingTask.setDescription(request.description());
        existingTask.setDueDate(request.dueDate());
        existingTask.setStatus(request.status());

        Task updateTask = taskRepository.save(existingTask);
        return taskMapper.toResponse(updateTask);
    }

    @Override
    public void deleteByCurrentUser(Long id) throws AccessDeniedException {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        Task task = taskRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Tarefa com id %d não encontrada ou não pertence ao usuário", id)
                ));

        taskRepository.delete(task);

    }

    @Override
    public TaskResponse completeTask(Long id) throws AccessDeniedException {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        Task task = taskRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow( () -> new ResourceNotFoundException(
                        String.format("Tarefa com id %d não encontrado ou não pertence ao usuário", id)
                ) );

        task.setStatus(TaskStatus.COMPLETED);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    @Override
    public List<TaskResponse> findOverdueTasks() throws AccessDeniedException {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        List<Task> tasks = taskRepository.findByUserIdAndDueDateBeforeAndStatusNot(
                currentUserId, LocalDate.now(), TaskStatus.COMPLETED
        );

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }
}
