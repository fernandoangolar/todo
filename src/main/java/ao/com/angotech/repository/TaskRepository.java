package ao.com.angotech.repository;

import ao.com.angotech.entity.Task;
import ao.com.angotech.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long id);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    long countByUserId(Long userId);

    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    List<Task> findByUserIdAndDueDate(Long userId, Date dueDate);
    List<Task> findByUserIdAndDueDateBeforeAndStatusNot(Long userId, LocalDate date, TaskStatus status);
}
