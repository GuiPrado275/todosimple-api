package com.guilhermepb.todosimple.services;

import com.guilhermepb.todosimple.models.User;
import com.guilhermepb.todosimple.repositories.UserRepository;
import com.guilhermepb.todosimple.services.exceptions.DataBindingViolationException;
import com.guilhermepb.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired                              //@Autowired: "Constructor" for when we want to instantiate an object, but
    private UserRepository userRepository;  //the interface can't to instantiate object, we use the notes of springboot
                                            //for to instantiate the objects

    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id); // if the user exists show it, else return:
        return user.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + id +
                ", Type: " + User.class.getName())); //the out if user is not found
    }

    @Transactional  //util for inputs in databases, in create or modification of database
    public User create (User obj){ //@Transactional creates a connection with database and save some data in memory
        obj.setId(null); // to ensure that a bad user can't use the id in username creation
        obj = this.userRepository.save(obj); //saves new object
        return obj;
    }

    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId()); //to ensure this user exists
        newObj.setPassword(obj.getPassword()); //update the password
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){ //for delete users
        findById(id); //to ensure that users exists
        try {
            this.userRepository.deleteById(id); //delete user
        } catch (Exception e) {
            throw new DataBindingViolationException("Cannot delete because this user has an entities relationship!");
        } //if user has a tasks, the script will not be deleted
    }

}
