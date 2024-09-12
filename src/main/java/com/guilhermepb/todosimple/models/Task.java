package com.guilhermepb.todosimple.models;


import lombok.*;

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
@Getter
@Setter
@EqualsAndHashCode
public class Task {

    public static final String TABLE_NAME = "task";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id is random (id)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne //Many tasks for one user
    @JoinColumn(name = "user_id", nullable = false, updatable = false) //this is for make reference of "user_id"
    private User user;                                                 //"user_id" of: (folder)models --> User

    @Column(name = "description", length = 255, nullable = false)
    @NotBlank                 //@NotNull + @NotEmpty == @NotBlank
    @Size(min = 1, max = 255) //limits for description
    private String description;

}
