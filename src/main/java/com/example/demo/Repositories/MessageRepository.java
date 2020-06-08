package com.example.demo.Repositories;

import com.example.demo.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    Set<Message> findAllBySentBy(String username);
//    void deleteAllByUsername(String username);

}
