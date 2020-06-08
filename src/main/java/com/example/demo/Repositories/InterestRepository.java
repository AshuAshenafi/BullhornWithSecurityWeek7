package com.example.demo.Repositories;

import com.example.demo.Model.News.Interest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends CrudRepository<Interest, Long> {
}
