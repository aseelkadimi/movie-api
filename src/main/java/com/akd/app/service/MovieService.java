package com.akd.app.service;

import com.akd.app.model.Movie;

public interface MovieService {
    Movie findById(Integer id);
    Movie findByName(String name);
    Iterable<Movie> findAll();
    Movie save(Movie movie);
    void update(Movie movie);
    void delete(Integer id);
}