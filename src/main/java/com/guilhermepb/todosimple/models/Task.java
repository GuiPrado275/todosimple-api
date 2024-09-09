package com.guilhermepb.todosimple.models;


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
import java.util.Objects;

@Entity
@Table(name = Task.TABLE_NAME) //table for database
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

    public Task() {
    }

    //constructor
    public Task(Long id, User user, String description) {
        this.id = id;
        this.user = user;
        this.description = description;
    }

    //getters and setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public @NotBlank @Size(min = 1, max = 255) String getDescription() {
        return this.description;
    }

    public void setDescription(@NotBlank @Size(min = 1, max = 255) String description) {
        this.description = description;
    }

    public Task id(Long id) {
        setId(id);
        return this;
    }

    public Task user(User user) {
        setUser(user);
        return this;
    }

    public Task description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public boolean equals(Object object) { //this 'equals' is to ensure:
        if (object == this){               // this.(id, user and description) == other.(id, username and password)
            return true;
        }
        if (!(object instanceof Task)){
            return false;
        }
        if (object == null){
            return false;
        }
        Task other = (Task) object;
        if (this.id == null){
            if (other.id != null){
                return false;
            }
            else if (!this.id.equals(other.id)){
                return false;
            }
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.user, other.user) &&
                Objects.equals(this.description, other.description);

    }

    @Override
    public int hashCode() { //storage and recuperation of objects, each object had a hashcode
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }
}
