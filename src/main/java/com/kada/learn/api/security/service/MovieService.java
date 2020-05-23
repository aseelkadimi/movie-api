package com.kada.learn.api.security.service;

import com.kada.learn.api.security.model.Movie;

public interface MovieService {
    Movie findById(Integer id);
    Movie findByName(String name);
    Iterable<Movie> findAll();
    Movie save(Movie movie);
    void update(Movie movie);
    void delete(Integer id);
}