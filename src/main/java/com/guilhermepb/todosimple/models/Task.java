package com.guilhermepb.todosimple.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = Task.TABLE_NAME) //table for database
@AllArgsConstructor  //constructor
@NoArgsConstructor   //constructor empty
@Data                //getters, setters, hashcode, toString ...
public class Task {

    public static final String TABLE_NAME = "task";

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id is random (id)
    private Long id;

    @ManyToOne //Many tasks for one user
    @JoinColumn(name = "user_id", nullable = false, updatable = false) //this is for make reference of "user_id"
    private User user;                                                 //"user_id" of: (folder)models --> User

    @Column(name = "description", length = 255, nullable = false)
    @Size(min = 1, max = 255) //limits for description
    @NotBlank                 //@NotNull + @NotEmpty == @NotBlank
    private String description;

}
