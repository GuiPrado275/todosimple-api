package com.guilhermepb.todosimple.services;

import com.guilhermepb.todosimple.models.Task;
import com.guilhermepb.todosimple.models.User;
import com.guilhermepb.todosimple.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id){
        Optional<Task> task = taskRepository.findById(id); // if the task exists show it, else return:
        return task.orElseThrow(() -> new RuntimeException("Task not found! Id: " + id +
                ", Type: " + Task.class.getName())); //the out if user is not found
    }

    @Transactional //util for inputs in databases, in create or modification of database
    public Task create(Task obj){ //@Transactional creates a connection with database and save some data in memory
        User user = userService.findById(obj.getUser().getId()); // to ensure that a bad user can't
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
            throw new RuntimeException("Task not found! Id: " + id);
        } //if task has a user, the script will not be deleted
    }

}
