package com.kada.learn.api.security.service;

import com.kada.learn.api.security.model.Movie;
import com.kada.learn.api.security.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie findById(Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public Movie findByName(String name) {
        return movieRepository.findByName(name).orElse(null);
    }

    @Override
    public Iterable<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public boolean update(Movie movie) {
        Movie update = movieRepository.save(movie);
        return update != null;
    }

    @Override
    public boolean delete(Integer id) {
        movieRepository.deleteById(id);
        return true;
    }
}