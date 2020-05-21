package com.kada.learn.api.security.service;

import com.kada.learn.api.security.model.Movie;

import java.util.Optional;

public interface MovieService {
    Optional<Movie> findById(Integer id);
    Optional<Movie> findByName(String name);
    Iterable<Movie> findAll();
    Movie save(Movie movie);
    boolean update(Movie movie);
    boolean delete(Integer id);
}