package com.guilhermepb.todosimple.repositories;

import com.guilhermepb.todosimple.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { //this is for search id, username...
    //User findByUsername(String username); - this isn't necessary because the "Jpa Repository" has many methods

    @Transactional(readOnly = true)
    User findByUsername(String username);  //For search user for username

}
