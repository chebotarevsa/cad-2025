package ru.bsuedu.cad.lab3.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab3.model.Priority;
import ru.bsuedu.cad.lab3.model.SubTask;
import ru.bsuedu.cad.lab3.model.Task;
import ru.bsuedu.cad.lab3.model.TaskHistory;
import ru.bsuedu.cad.lab3.repository.SubTaskRepository;
import ru.bsuedu.cad.lab3.repository.TaskRepository;
import ru.bsuedu.cad.lab3.repository.UserRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       SubTaskRepository subTaskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.subTaskRepository = subTaskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addTask(String title, String description, LocalDate dueDate,
                        Priority priority, String category, String username) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setPriority(priority);
        task.setCategory(category == null || category.isBlank() ? null : category.trim());
        userRepository.findByName(username).ifPresent(task::setOwner);
        task.addHistory(new TaskHistory(null, state(task)));
        taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc();
    }

    /** Вместе с задачей каскадно удаляются её подзадачи и записи истории. */
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public void updateTask(Long id, boolean completed) {
        taskRepository.findById(id).ifPresent(task -> {
            if (task.isCompleted() != completed) {
                String oldState = state(task);
                task.setCompleted(completed);
                task.addHistory(new TaskHistory(oldState, state(task)));
            }
        });
    }

    @Transactional
    public void addSubTask(Long taskId, String description) {
        taskRepository.findById(taskId)
                .ifPresent(task -> task.addSubTask(new SubTask(description)));
    }

    @Transactional
    public void updateSubTask(Long subTaskId, boolean completed) {
        subTaskRepository.findById(subTaskId)
                .ifPresent(subTask -> subTask.setCompleted(completed));
    }

    private String state(Task task) {
        return task.isCompleted() ? "Выполнена" : "Не выполнена";
    }
}
