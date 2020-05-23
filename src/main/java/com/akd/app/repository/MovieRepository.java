package com.akd.app.repository;

import com.akd.app.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Integer> {

    Optional<Movie> findByName(String name);
}
