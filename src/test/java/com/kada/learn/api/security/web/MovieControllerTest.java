package com.kada.learn.api.security.web;

import com.kada.learn.api.security.model.Movie;
import com.kada.learn.api.security.service.MovieService;
import com.kada.learn.api.security.utils.AssertionUtils;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

class MovieControllerTest {

    @Mock
    private MovieService service;

    private final EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private MovieController underTest;

    @Test
    public void testGetMoviesWhenSuccess(){
        underTest = new MovieController(service);
        List<Movie> expectedMovies = Collections.singletonList(new Movie());
        Mockito.when(service.findAll()).thenReturn(expectedMovies);

        ResponseEntity<?> actual = underTest.getMovies();

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(AssertionUtils.asJsonString(expectedMovies), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    public void testGetMoviesWhenFailure(){
        underTest = new MovieController(service);
        List<Movie> expectedMovies = Collections.emptyList();
        Mockito.when(service.findAll()).thenReturn(expectedMovies);

        ResponseEntity<?> actual = underTest.getMovies();

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(AssertionUtils.asJsonString(expectedMovies), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    @DisplayName("GET /movie/movieId - Found")
    public void testGetMovieWhenFound(){
        underTest = new MovieController(service);
        Movie expectedMovie = easyRandom.nextObject(Movie.class);
        doReturn(Optional.of(expectedMovie)).when(service).findById(expectedMovie.getId());

        ResponseEntity<?> actual = underTest.getMovie(expectedMovie.getId());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertTrue(AssertionUtils.assertHeaders(expectedMovie, actual));
        Assertions.assertEquals(AssertionUtils.asJsonString(expectedMovie), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    @DisplayName("GET /movie/movieId - Not Found")
    public void testGetMovieWhenNotFound(){
        underTest = new MovieController(service);
        Movie expectedMovie = easyRandom.nextObject(Movie.class);
        doReturn(Optional.empty()).when(service).findById(expectedMovie.getId());

        ResponseEntity<?> actual = underTest.getMovie(expectedMovie.getId());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }
}