package com.guilhermepb.todosimple.repositories;

import com.guilhermepb.todosimple.models.Task;
import com.guilhermepb.todosimple.models.projection.TaskProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // form 1 -
    List<TaskProjection> findByUser_Id(Long id); //search id
    //form 1 is very simple

    //form 2:
    //@Query(value = "SELECT t FROM Task t WHERE t.user.id = : id") this is for search id
    //List<Task> findByUser_Id(@Param("id")Long id);
    // form 2 is little simple

    //form 3:
    //@Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true) //this is for search id
    //List<Task> findByUser_Id(@Param("id")Long id);
    // form 3 isn't simple

}
