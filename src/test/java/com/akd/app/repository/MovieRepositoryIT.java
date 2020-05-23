package com.akd.app.repository;

import com.akd.app.Application;
import com.akd.app.model.Movie;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class MovieRepositoryIT {

    @Autowired
    private MovieRepository movieRepository;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void testCreateAndGetById() {
        // Given
        Movie expectedMovie = easyRandom.nextObject(Movie.class);

        // When
        Movie savedMovie = movieRepository.save(expectedMovie);
        Movie found = movieRepository.findById(savedMovie.getId()).orElse(null);

        // Then
        Assertions.assertNotNull(found);
        Assertions.assertEquals(savedMovie, found);
    }

    @Test
    public void testCreateAndGetByName() {
        // Given
        Movie expectedMovie = easyRandom.nextObject(Movie.class);

        // When
        Movie savedMovie = movieRepository.save(expectedMovie);
        Movie found = movieRepository.findByName(expectedMovie.getName()).orElse(null);

        // Then
        Assertions.assertNotNull(found);
        Assertions.assertEquals(expectedMovie.getName(), found.getName());
        Assertions.assertEquals(savedMovie, found);
    }

    @Test
    public void testCreateMoviesAndGetAll() {
        // Given
        List<Movie> randomMovies = easyRandom
                .objects(Movie.class,10)
                .collect(Collectors.toList());
        // When
        List<Movie> savedMovies = saveMovies(randomMovies);
        List<Movie> foundMovies = new ArrayList<>();
        Iterable<Movie> iterable = movieRepository.findAll();
        iterable.forEach(foundMovies::add);

        // Then
        Assertions.assertFalse(savedMovies.isEmpty());
        Assertions.assertFalse(foundMovies.isEmpty());
        savedMovies.forEach(movie -> {
            Assertions.assertTrue(foundMovies.contains(movie));
        });
    }

    @Test
    public void testUpdateSuccess() {
        // Given
        Movie expectedMovie = easyRandom.nextObject(Movie.class);
        Movie savedMovie = movieRepository.save(expectedMovie);
        Movie found = movieRepository.findById(savedMovie.getId()).orElse(new Movie());

        String oldName = expectedMovie.getName();
        String newName = easyRandom.nextObject(String.class);
        found.setName(newName);

        // When
        Movie updated = movieRepository.save(found);
        found = movieRepository.findById(savedMovie.getId()).orElse(new Movie());

        // Then
        Assertions.assertNotEquals(newName, oldName);
        Assertions.assertEquals(newName, found.getName());
        Assertions.assertEquals(updated, found);
    }

    private List<Movie> saveMovies(List<Movie> movies){
        List<Movie> saved = new ArrayList<>();

        movies.forEach(movie -> {
            saved.add(movieRepository.save(movie));
        });
    return saved;
    }
}