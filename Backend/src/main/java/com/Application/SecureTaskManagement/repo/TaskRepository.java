package com.Application.SecureTaskManagement.repo;




import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Application.SecureTaskManagement.entity.Task;
import com.Application.SecureTaskManagement.entity.User;
import com.Application.SecureTaskManagement.enums.Priority;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user, org.springframework.data.domain.Sort sort);
    List<Task> findByCompletedAndUser(boolean completed, User user);
    List<Task> findByPriorityAndUser(Priority priority, User user, org.springframework.data.domain.Sort sort);

}
