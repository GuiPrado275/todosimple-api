package com.guilhermepb.todosimple.services;

import com.guilhermepb.todosimple.models.Task;
import com.guilhermepb.todosimple.models.User;
import com.guilhermepb.todosimple.models.enums.ProfileEnum;
import com.guilhermepb.todosimple.models.projection.TaskProjection;
import com.guilhermepb.todosimple.repositories.TaskRepository;
import com.guilhermepb.todosimple.security.UserSpringSecurity;
import com.guilhermepb.todosimple.services.exceptions.AuthorizationException;
import com.guilhermepb.todosimple.services.exceptions.DataBindingViolationException;
import com.guilhermepb.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id){ // if the task exists show it, else return:
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Task not found! Id: " + id + ", Type: " + Task.class.getName())); //the out if user is not found

        UserSpringSecurity userSpringSecurity = UserService.authenticated(); //for authenticate if user is not logged
        if (Objects.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN)
                && !userHasTask(userSpringSecurity, task)) { //for authenticate if user is not logged or not adm,
            throw new AuthorizationException("Access denied"); //or not be the owner of task
        }

        return task;
    }

    public List<TaskProjection> findAllByUser(){      //search all tasks of one user
        UserSpringSecurity userSpringSecurity = UserService.authenticated(); //for authenticate if user is not logged
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Access denied");

        List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId()); //find the tasks
        return tasks;
    }

    @Transactional //util for inputs in databases, in create or modification of database
    public Task create(Task obj){ //@Transactional creates a connection with database and save some data in memory
        UserSpringSecurity userSpringSecurity = UserService.authenticated(); //for authenticate if user is not logged
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Access denied");
        }
        User user = userService.findById(userSpringSecurity.getId()); // to ensure that a bad user can't
        obj.setId(null);                                           //use the id in task creation
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj){
        Task newObj = findById(obj.getId()); //to ensure this user exists
        newObj.setDescription(obj.getDescription()); //update the task
        return taskRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id); //to ensure that tasks exists
        try{
            taskRepository.deleteById(id); //delete task
        }
        catch (Exception e){
            throw new DataBindingViolationException("Task not found! Id: " + id);
        } //if task has a user, the script will not be deleted
    }

    private boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){ //verify if user have a task
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }

}
