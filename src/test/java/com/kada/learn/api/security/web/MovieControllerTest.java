//package com.kada.learn.api.security.web;
//
//import com.kada.learn.api.security.model.Movie;
//import com.kada.learn.api.security.service.MovieService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Collections;
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//class MovieControllerTest {
//
//    @Mock
//    private MovieService service;
//
//    private MovieController underTest;
//
//    @BeforeEach
//    public void setUp(){
//        underTest = new MovieController(service);
//    }
//
//    @Test
//    public void testGetMovieWhenSuccess(){
//        List<Movie> expectedMovies = Collections.singletonList(new Movie());
//        Mockito.when(service.findAll()).thenReturn(expectedMovies);
//
//        ResponseEntity<?> actual = underTest.getMovies();
//
//        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
//    }
//}