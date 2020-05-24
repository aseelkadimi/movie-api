package com.akd.app.web;


import com.akd.app.model.Movie;
import com.akd.app.service.MovieService;
import com.akd.app.utils.AssertionUtils;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIT {

    @MockBean
    private MovieService service;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom easyRandom = new EasyRandom();

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @BeforeAll
    static void beforeAll() {
        // Spring's dates are configured to GMT, so adjust our timezone accordingly
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Test
    @DisplayName("GET /movie/movieId - Found")
    void testGetMovieByIdFound() throws Exception {
        // Given
        Movie movie = easyRandom.nextObject(Movie.class);
        doReturn(movie).when(service).findById(movie.getId());

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get("/movie/{id}", movie.getId()))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(AssertionUtils.assertHeaders(movie, response));
        Assertions.assertTrue(AssertionUtils.assertBody(movie, response));
    }

    @Test
    @DisplayName("GET /movie/movieId - Not Found")
    void testGetMovieByIdNotFound() throws Exception {
        // Given
        Movie movie = easyRandom.nextObject(Movie.class);
        doReturn(null).when(service).findById(movie.getId());

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get("/movie/{id}", movie.getId()))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("POST /movie - Success")
    void testCreateMovieSuccess() throws Exception {
        // Given
        Movie movie = easyRandom.nextObject(Movie.class);
        doReturn(movie).when(service).save(any());

        // When
        MockHttpServletResponse response =  mockMvc.perform(post("/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AssertionUtils.asJsonString(movie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        Assertions.assertTrue(AssertionUtils.assertHeaders(movie, response));
        Assertions.assertTrue(AssertionUtils.assertBody(movie, response));
    }

    @Test
    @DisplayName("POST /movie - Failed")
    void testCreateMovieFailed() throws Exception {
        // Given
        Movie movie = easyRandom.nextObject(Movie.class);
        doReturn(null).when(service).save(any());

        // When
        MockHttpServletResponse response =  mockMvc.perform(post("/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AssertionUtils.asJsonString(movie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    @DisplayName("PUT /movie/1 - Success")
    void testUpdateMovieSuccess() throws Exception {
        // Given
        Movie putMovie = new Movie();
        putMovie.setName("Matrix");
        putMovie.setYearOfRelease("2002");

        Movie getMovie = new Movie(1, 1, "Matrix", "Us", "2020", "30k", "Good movie");

        doReturn(getMovie).when(service).findById(anyInt());

        // When
        MockHttpServletResponse response =  mockMvc.perform(put("/movie/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(AssertionUtils.asJsonString(putMovie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(AssertionUtils.assertHeaders(getMovie, response));
        Assertions.assertTrue(AssertionUtils.assertBody(getMovie, response));
    }

    @Test
    @DisplayName("PUT /movie/1 - Not Found")
    void testUpdateMovieNotFound() throws Exception {
        // Given
        Movie putMovie = new Movie();
        putMovie.setName("Matrix");
        putMovie.setYearOfRelease("2002");

        doReturn(null).when(service).findById(1);

        // When
        MockHttpServletResponse response =  mockMvc.perform(put("/movie/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(AssertionUtils.asJsonString(putMovie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Disabled
    @Test
    @DisplayName("DELETE /movie/1 - Success")
    void testDeleteMovieSuccess() throws Exception {
        // Given
        Movie getMovie = new Movie(1, "Matrix", "Us", "2020", "30k", "Good movie");

        doReturn(getMovie).when(service).findById(1);

        // When
        MockHttpServletResponse response =  mockMvc
                .perform(delete("/movie/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Disabled
    @Test
    @DisplayName("DELETE /movie/1 - Not Found")
    @WithMockUser(value = "admin")
    void testDeleteMovieNotFound() throws Exception {
        // Given

        doReturn(null).when(service).findById(1);

        // When
        MockHttpServletResponse response =  mockMvc
                .perform(delete("/movie/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}