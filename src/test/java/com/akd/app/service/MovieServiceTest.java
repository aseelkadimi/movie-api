package com.akd.app.service;

import com.akd.app.model.Movie;
import com.akd.app.repository.MovieRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

class MovieServiceTest {

    private MovieService underTest;

    @Mock
    private MovieRepository movieRepository;

    private final EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new MovieServiceImpl(movieRepository);
    }

    @Test
    @DisplayName("Method find by Id - Success")
    public void testFindByIdSuccess(){
        Movie expected = easyRandom.nextObject(Movie.class);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Movie actual = underTest.findById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method find by Id - Failed")
    public void testFindByIdFailed(){
        Movie expected = easyRandom.nextObject(Movie.class);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.empty());

        Movie actual = underTest.findByName(expected.getName());
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Method find by Name - Success")
    public void testFindByNameSuccess(){
        Movie expected = easyRandom.nextObject(Movie.class);
        when(movieRepository.findByName(anyString())).thenReturn(Optional.of(expected));

        Movie actual = underTest.findByName(expected.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method find by Name - Failed")
    public void testFindByNameFailed(){
        Movie expected = easyRandom.nextObject(Movie.class);
        when(movieRepository.findByName(anyString())).thenReturn(Optional.empty());

        Movie actual = underTest.findById(expected.getId());
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Method find all - return list")
    public void testFindAllSuccess(){
        List<Movie> expected = easyRandom
                .objects(Movie.class,10)
                .collect(Collectors.toList());

        when(movieRepository.findAll()).thenReturn(expected);

        List<Movie> actual = (List<Movie>) underTest.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method find all - return empty list")
    public void testFindAllEmpty(){
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        List<Movie> actual = (List<Movie>) underTest.findAll();
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Method save - Success")
    public void testSaveSuccess(){
        Movie toSave = easyRandom.nextObject(Movie.class);

        when(movieRepository.save(toSave)).thenReturn(toSave);

        Movie actual = underTest.save(toSave);
        Assertions.assertEquals(toSave, actual);
    }

    @Test
    @DisplayName("Method update - Success")
    public void testUpdateSuccess(){
        Movie toSave = easyRandom.nextObject(Movie.class);

        when(movieRepository.save(toSave)).thenReturn(toSave);

        underTest.update(toSave);
    }

    @Test
    @DisplayName("Method delete - Success")
    public void testDeleteSuccess(){
        underTest.delete(1);
    }
}