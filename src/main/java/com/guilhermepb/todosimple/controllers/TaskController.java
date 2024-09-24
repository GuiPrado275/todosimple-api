package com.guilhermepb.todosimple.controllers;

import com.guilhermepb.todosimple.models.Task;
import com.guilhermepb.todosimple.services.TaskService;
import com.guilhermepb.todosimple.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) { //task type response entity
        Task obj = taskService.findById(id);                      //@PathVariable: is a variable in ("/{id}")
        return ResponseEntity.ok(obj);                       // .ok() - If there is no error, continue
                                                            // .body() - Is the "body" of response, local of data
    }

    @PostMapping
    @Validated                              //validate the Task
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj){ //@Valid: for validate  @RequestBody - for data
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest() //It is a constructor, which will take the context
                .path("/{id}").buildAndExpand(obj.getId()).toUri(); //  of the request (task and local host),
        return ResponseEntity.created(uri).build(); //add a path (id and getId()), showing the URI inside .created()
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> findAllByUser(){ //search all tasks by userId
        List<Task> objs = this.taskService.findAllByUser();     //tasks list to be returned
        return ResponseEntity.ok().body(objs);
    } //create an error if the user doesn't exist

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id){
        obj.setId(id);
        this.taskService.update(obj); //for update task
        return ResponseEntity.noContent().build(); //noContend() because we are not returning any data, only updating
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.taskService.delete(id);                    //for delete task
        return ResponseEntity.noContent().build(); //noContend() because we are not returning any data, only deleting
    }

}
