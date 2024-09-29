package com.guilhermepb.todosimple.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT) //if user has a tasks, the user don't be deleted
public class DataBindingViolationException extends DataIntegrityViolationException {

    public DataBindingViolationException(String message) {
        super(message);
    }

}