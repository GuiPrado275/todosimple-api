package com.guilhermepb.todosimple.controllers;

import com.guilhermepb.todosimple.models.User;
import com.guilhermepb.todosimple.models.dto.UserCreateDTO;
import com.guilhermepb.todosimple.models.dto.UserUpdateDTO;
import com.guilhermepb.todosimple.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController                  //for Controller
@RequestMapping("/user")     //base route
@Validated                      //validations
public class UserController {

    @Autowired                        //injection of dependence
    private UserService userService;

    // localhost:8080/user/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){ //user type response entity
        User obj = this.userService.findById(id); //@PathVariable: is a variable in ("/{id}")
        return ResponseEntity.ok().body(obj);     // .ok() - If there is no error, continue
    }                                             // .body() - Is the "body" of response, local of data

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj){//@Valid:forvalidade@RequestBody-for dates
        User user = this.userService.fromDTO(obj);
        User newUser = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest() //It is a constructor, which will take the context
                .path("/{id}").buildAndExpand(newUser.getId()).toUri(); //  of the request (user and local host),
        return ResponseEntity.created(uri).build(); //add a path (id and getId()), showing the URI inside .created()
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable Long id){
        obj.setId(id);
        User user = this.userService.fromDTO(obj);
        this.userService.update(user);  //for update user
        return ResponseEntity.noContent().build(); //noContend() because we are not returning any data, only updating
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.userService.delete(id);                                //for delete user
        return ResponseEntity.noContent().build(); //noContend() because we are not returning any data, only deleting
    }

}
