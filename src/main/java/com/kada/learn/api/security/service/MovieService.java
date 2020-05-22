package com.kada.learn.api.security.service;

import com.kada.learn.api.security.model.Movie;

public interface MovieService {
    Movie findById(Integer id);
    Movie findByName(String name);
    Iterable<Movie> findAll();
    Movie save(Movie movie);
    boolean update(Movie movie);
    boolean delete(Integer id);
}