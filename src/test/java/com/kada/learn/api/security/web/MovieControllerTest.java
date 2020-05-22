package com.kada.learn.api.security.web;

import com.kada.learn.api.security.exception.AlreadyExistException;
import com.kada.learn.api.security.model.Movie;
import com.kada.learn.api.security.model.MovieDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class MovieControllerTest {

    @Mock
    private MovieService service;

    private final EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new MovieController(service);
    }

    private MovieController underTest;

    @Test
    public void testGetMoviesWhenSuccess(){
        List<Movie> expectedMovies = Collections.singletonList(new Movie());
        Mockito.when(service.findAll()).thenReturn(expectedMovies);

        ResponseEntity<?> actual = underTest.getMovies();

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(AssertionUtils.asJsonString(expectedMovies), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    public void testGetMoviesWhenFailure(){
        List<Movie> expectedMovies = Collections.emptyList();
        Mockito.when(service.findAll()).thenReturn(expectedMovies);

        ResponseEntity<?> actual = underTest.getMovies();

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(AssertionUtils.asJsonString(expectedMovies), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    @DisplayName("GET /movie/movieId - Found")
    public void testGetMovieWhenFound(){
        Movie expectedMovie = easyRandom.nextObject(Movie.class);
        doReturn(expectedMovie).when(service).findById(expectedMovie.getId());

        ResponseEntity<Movie> actual = underTest.getMovie(expectedMovie.getId());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertTrue(AssertionUtils.assertHeaders(expectedMovie, actual));
        Assertions.assertEquals(AssertionUtils.asJsonString(expectedMovie), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    @DisplayName("GET /movie/movieId - Not Found")
    public void testGetMovieWhenNotFound(){
        Movie expectedMovie = easyRandom.nextObject(Movie.class);
        doReturn(null).when(service).findById(expectedMovie.getId());

        ResponseEntity<?> actual = underTest.getMovie(expectedMovie.getId());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        Assertions.assertNull(actual.getBody());
    }

    @Test
    @DisplayName("POST /movie - Success")
    public void testCreateMovieSuccess(){
        ModelMapper modelMapper = new ModelMapper();

        // Given
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);
        Movie movie = modelMapper.map(movieDto, Movie.class);

        doReturn(null).when(service).findByName(any());
        doReturn(movie).when(service).save(any());

        ResponseEntity<Movie> actual = underTest.createMovie(movieDto);

        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        Assertions.assertTrue(AssertionUtils.assertHeaders(movie, actual));
        Assertions.assertEquals(AssertionUtils.asJsonString(movie), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    @DisplayName("POST /movie - Already exist")
    public void testCreateMovieAlreadyExist(){
        ModelMapper modelMapper = new ModelMapper();

        // Given
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);
        Movie movie = modelMapper.map(movieDto, Movie.class);

        doReturn(movie).when(service).findByName(any());
        Assertions.assertThrows(AlreadyExistException.class, () -> {
            underTest.createMovie(movieDto);
        });
    }

    @Test
    @DisplayName("PUT /movie/1 - Does not exist")
    public void testUpdateMovieDoesNotExist(){
        // Given
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);
        doReturn(null).when(service).findByName(any());

        ResponseEntity<Movie> actual = underTest.updateMovie(movieDto, movieDto.getId(), movieDto.getVersion());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    @DisplayName("PUT /movie/1 - If match does not match")
    public void testUpdateMovieIfMatchDoesNotMatch(){
        // Given
        ModelMapper modelMapper = new ModelMapper();
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);
        Movie movie = modelMapper.map(movieDto, Movie.class);
        doReturn(movie).when(service).findById(any());

        // When
        ResponseEntity<Movie> actual = underTest.updateMovie(
                movieDto, movieDto.getId(), 3);

        // Then
        Assertions.assertEquals(HttpStatus.CONFLICT, actual.getStatusCode());
    }

    @Test
    @DisplayName("PUT /movie/1 - Failed")
    public void testUpdateMovieFailed(){
        ModelMapper modelMapper = new ModelMapper();

        // Given
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);
        Movie movie = modelMapper.map(movieDto, Movie.class);

        doReturn(movie).when(service).findById(any());
        doReturn(false).when(service).update(any());

        // When
        ResponseEntity<Movie> actual = underTest.updateMovie(movieDto, movieDto.getId(), movieDto.getVersion());

        // Then
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }

    @Test
    @DisplayName("PUT /movie/1 - Success")
    public void testUpdateMovieSuccess(){
        ModelMapper modelMapper = new ModelMapper();

        // Given
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);
        Movie movie = modelMapper.map(movieDto, Movie.class);

        doReturn(movie).when(service).findById(any());
        doReturn(true).when(service).update(any());

        // When
        ResponseEntity<Movie> actual = underTest.updateMovie(movieDto, movieDto.getId(), movieDto.getVersion());

        // Then
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertTrue(AssertionUtils.assertHeaders(movie, actual));
        Assertions.assertEquals(AssertionUtils.asJsonString(movie), AssertionUtils.asJsonString(actual.getBody()));
    }

    @Test
    @DisplayName("PUT /movie/1 - Does not exist")
    void testDeleteMovieDoesNotExist() throws Exception {
        // Given
        MovieDto movieDto = easyRandom.nextObject(MovieDto.class);
        doReturn(null).when(service).findById(any());

        ResponseEntity<Movie> actual = underTest.deleteMovie(movieDto.getId());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    @DisplayName("PUT /movie/1 - Failure")
    void testDeleteMovieFailure() throws Exception {
        // Given
        Movie movie = easyRandom.nextObject(Movie.class);

        doReturn(movie).when(service).findById(any());
        doReturn(false).when(service).delete(any());

        ResponseEntity<Movie> actual = underTest.deleteMovie(movie.getId());

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }

    @Test
    @DisplayName("PUT /movie/1 - Success")
    void testDeleteMovieSuccess() throws Exception {
        // Given
        Movie movie = easyRandom.nextObject(Movie.class);

        doReturn(movie).when(service).findById(any());
        doReturn(true).when(service).delete(any());

        ResponseEntity<Movie> actual = underTest.deleteMovie(movie.getId());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
    }
}