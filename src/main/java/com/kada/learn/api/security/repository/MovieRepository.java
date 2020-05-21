package com.kada.learn.api.security.repository;

import com.kada.learn.api.security.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Integer> {

    Optional<Movie> findByName(String name);
}
