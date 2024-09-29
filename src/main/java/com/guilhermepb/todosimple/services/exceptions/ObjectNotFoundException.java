package com.guilhermepb.todosimple.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.NOT_FOUND) //the errors of TaskService and UserService are 404 now
public class ObjectNotFoundException  extends EntityNotFoundException {

    public ObjectNotFoundException(String message) {
        super(message);
    }

}
