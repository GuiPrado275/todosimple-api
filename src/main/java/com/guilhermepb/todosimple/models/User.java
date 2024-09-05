package com.guilhermepb.todosimple.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = User.TABLE_NAME) //table for database
public class User {
    public interface CreateUser{}
    public interface UpdateUser{} //update user is only for password

    public static final String TABLE_NAME = "user";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id is random (User id)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true) // limits for the username
    @NotNull(groups = CreateUser.class) //the username only can be created and he cant be null or empyt
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100) //limits of length
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //write_only:front side don't have access of password
    @Column(name = "password", length = 60, nullable = false) //limits
    @NotNull(groups = {CreateUser.class, UpdateUser.class}) // password can be created and updated
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 60) //limits of length
    private String password;

    @OneToMany(mappedBy = "user") // One user can have many tasks
    private List<Task> tasks = new ArrayList<Task>(); //tasks list

    public User(){
    }

    //constructor
    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }


    //getters and setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(groups = CreateUser.class)
            @NotEmpty(groups = CreateUser.class) @Size(groups = CreateUser.class, min = 2,
            max = 100) String getUsername() {
        return this.username;
    }

    public void setUsername(@NotNull(groups = CreateUser.class)
                            @NotEmpty(groups = CreateUser.class) @Size(groups = CreateUser.class, min = 2,
                            max = 100) String username) {
        this.username = username;
    }

    public @NotNull(groups = {CreateUser.class, UpdateUser.class})
                              @NotEmpty(groups = {CreateUser.class, UpdateUser.class}) @Size(groups =
                              {CreateUser.class, UpdateUser.class}, min = 8, max = 60) String getPassword() {
        return this.password;
    }

    public void setPassword(@NotNull(groups = {CreateUser.class, UpdateUser.class})
                            @NotEmpty(groups = {CreateUser.class, UpdateUser.class}) @Size(groups =
                            {CreateUser.class, UpdateUser.class}, min = 8, max = 60) String password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    //this 'equals' is to ensure: this.(id, username and password) == other.(id, username and password)
    @Override
    public boolean equals(Object object) {
        if (object == this){
            return true;
        }
        if (!(object instanceof User)){
            return false;
        }
        if (object == null){
            return false;
        }
        User other = (User) object;
        if (this.id == null){
            if (other.id != null){
                return false;
            }
            else if (!this.id.equals(other.id)){
                return false;
            }
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username)
                && Objects.equals(this.password, other.password);
    }

    //hashCode
    @Override
    public int hashCode() { //storage and recuperation of objects, each object had a hashcode
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

}
