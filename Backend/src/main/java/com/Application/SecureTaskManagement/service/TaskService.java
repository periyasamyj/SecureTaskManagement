package com.Application.SecureTaskManagement.service;




import java.util.List;

import com.Application.SecureTaskManagement.dto.Response;
import com.Application.SecureTaskManagement.dto.TaskRequest;
import com.Application.SecureTaskManagement.entity.Task;

public interface TaskService {

    Response<Task> createTask(TaskRequest taskRequest);
    Response<List<Task>> getAllMyTasks();
    Response<Task> getTaskById(Long id);
    Response<Task> updateTask(TaskRequest taskRequest);
    Response<Void> deleteTask(Long id);
    Response<List<Task>> getMyTasksByCompletionStatus(boolean completed);
    Response<List<Task>> getMyTasksByPriority(String priority);

}
