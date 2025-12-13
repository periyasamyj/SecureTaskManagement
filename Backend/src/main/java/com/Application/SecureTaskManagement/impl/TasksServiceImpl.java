package com.Application.SecureTaskManagement.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Application.SecureTaskManagement.dto.Response;
import com.Application.SecureTaskManagement.dto.TaskRequest;
import com.Application.SecureTaskManagement.entity.Task;
import com.Application.SecureTaskManagement.entity.User;
import com.Application.SecureTaskManagement.enums.Priority;
import com.Application.SecureTaskManagement.exceptions.NotFoundException;
import com.Application.SecureTaskManagement.repo.TaskRepository;
import com.Application.SecureTaskManagement.service.TaskService;
import com.Application.SecureTaskManagement.service.UserService;


@Service
public class TasksServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    // Manual logger instead of @Slf4j
    private static final Logger log = Logger.getLogger(TasksServiceImpl.class.getName());

    // Manual constructor instead of @RequiredArgsConstructor
    public TasksServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public Response<Task> createTask(TaskRequest taskRequest) {
        log.info("INSIDE createTask()");

        User user = userService.getCurrentLoggedInUser();

        Task taskToSave = new Task();
        taskToSave.setTitle(taskRequest.getTitle());
        taskToSave.setDescription(taskRequest.getDescription());
        taskToSave.setCompleted(taskRequest.getCompleted());
        taskToSave.setPriority(taskRequest.getPriority());
        taskToSave.setDueDate(taskRequest.getDueDate());
        taskToSave.setCreatedAt(LocalDateTime.now());
        taskToSave.setUpdatedAt(LocalDateTime.now());
        taskToSave.setUser(user);

        Task savedTask = taskRepository.save(taskToSave);

        Response<Task> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Task Created Successfully");
        response.setData(savedTask);

        return response;
    }

    @Override
    @Transactional
    public Response<List<Task>> getAllMyTasks() {
        log.info("inside getAllMyTasks()");
        User currentUser = userService.getCurrentLoggedInUser();

        List<Task> tasks = taskRepository.findByUser(currentUser, Sort.by(Sort.Direction.DESC, "id"));

        Response<List<Task>> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Tasks retrieved successfully");
        response.setData(tasks);

        return response;
    }

    @Override
    public Response<Task> getTaskById(Long id) {
        log.info("inside getTaskById()");

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tasks not found"));

        Response<Task> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Task retrieved successfully");
        response.setData(task);

        return response;
    }

    @Override
    public Response<Task> updateTask(TaskRequest taskRequest) {
        log.info("inside updateTask()");

        Task task = taskRepository.findById(taskRequest.getId())
                .orElseThrow(() -> new NotFoundException("Tasks not found"));

        if (taskRequest.getTitle() != null) task.setTitle(taskRequest.getTitle());
        if (taskRequest.getDescription() != null) task.setDescription(taskRequest.getDescription());
        if (taskRequest.getCompleted() != null) task.setCompleted(taskRequest.getCompleted());
        if (taskRequest.getPriority() != null) task.setPriority(taskRequest.getPriority());
        if (taskRequest.getDueDate() != null) task.setDueDate(taskRequest.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);

        Response<Task> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Task updated successfully");
        response.setData(updatedTask);

        return response;
    }

    @Override
    public Response<Void> deleteTask(Long id) {
        log.info("inside delete task");
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task does not exist");
        }
        taskRepository.deleteById(id);

        Response<Void> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Task deleted successfully");

        return response;
    }

    @Override
    @Transactional
    public Response<List<Task>> getMyTasksByCompletionStatus(boolean completed) {
        log.info("inside getMyTasksByCompletionStatus()");

        User currentUser = userService.getCurrentLoggedInUser();

        List<Task> tasks = taskRepository.findByCompletedAndUser(completed, currentUser);

        Response<List<Task>> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Tasks filtered by completion status for user");
        response.setData(tasks);

        return response;
    }

    @Override
    public Response<List<Task>> getMyTasksByPriority(String priority) {
        log.info("inside getMyTasksByPriority()");

        User currentUser = userService.getCurrentLoggedInUser();

        Priority priorityEnum = Priority.valueOf(priority.toUpperCase());

        List<Task> tasks = taskRepository.findByPriorityAndUser(priorityEnum, currentUser, Sort.by(Sort.Direction.DESC, "id"));

        Response<List<Task>> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Tasks filtered by priority for user");
        response.setData(tasks);

        return response;
    }
}
