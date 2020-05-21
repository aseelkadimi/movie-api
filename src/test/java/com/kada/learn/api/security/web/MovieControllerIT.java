package com.kada.learn.api.security.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kada.learn.api.security.model.Movie;
import com.kada.learn.api.security.service.MovieService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;
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

    /**
     * Create a DateFormat that we can use to compare SpringMVC returned dates to expected values.
     */
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
        doReturn(Optional.of(movie)).when(service).findById(movie.getId());

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get("/movie/{id}", movie.getId()))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(assertHeaders(movie, response));
        Assertions.assertTrue(assertBody(movie, response));
    }

    @Test
    @DisplayName("GET /movie/movieId - Not Found")
    void testGetMovieByIdNotFound() throws Exception {
        // Given
        Movie movie = easyRandom.nextObject(Movie.class);
        doReturn(Optional.empty()).when(service).findById(movie.getId());

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
                .content(asJsonString(movie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        Assertions.assertTrue(assertHeaders(movie, response));
        Assertions.assertTrue(assertBody(movie, response));
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
                .content(asJsonString(movie)))
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

        doReturn(Optional.of(getMovie)).when(service).findById(anyInt());
        doReturn(true).when(service).update(any());

        // When
        MockHttpServletResponse response =  mockMvc.perform(put("/movie/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(asJsonString(putMovie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(assertHeaders(getMovie, response));
        Assertions.assertTrue(assertBody(getMovie, response));
    }

    @Test
    @DisplayName("PUT /movie/1 - Not Found")
    void testUpdateMovieNotFound() throws Exception {
        // Given
        Movie putMovie = new Movie();
        putMovie.setName("Matrix");
        putMovie.setYearOfRelease("2002");

        doReturn(Optional.empty()).when(service).findById(1);

        // When
        MockHttpServletResponse response =  mockMvc.perform(put("/movie/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(asJsonString(putMovie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("PUT /movie/1 - Failure")
    void testUpdateMovieFailure() throws Exception {
        // Given
        Movie putMovie = new Movie();
        putMovie.setName("Matrix");
        putMovie.setYearOfRelease("2002");

        Movie getMovie = new Movie(1,1, "Matrix", "Us", "2020", "30k", "Good movie");

        doReturn(Optional.of(getMovie)).when(service).findById(getMovie.getId());
        doReturn(false).when(service).update(any());

        // When
        MockHttpServletResponse response =  mockMvc.perform(put("/movie/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(asJsonString(putMovie)))
                .andReturn().getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    @DisplayName("DELETE /movie/1 - Success")
    void testDeleteMovieSuccess() throws Exception {
        // Given
        Movie getMovie = new Movie(1, "Matrix", "Us", "2020", "30k", "Good movie");

        doReturn(Optional.of(getMovie)).when(service).findById(1);
        doReturn(true).when(service).delete(1);

        // When
        MockHttpServletResponse response =  mockMvc
                .perform(delete("/movie/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("DELETE /movie/1 - Not Found")
    void testDeleteMovieNotFound() throws Exception {
        // Given

        doReturn(Optional.empty()).when(service).findById(1);

        // When
        MockHttpServletResponse response =  mockMvc
                .perform(delete("/movie/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("DELETE /movie/1 - Failure")
    void testDeleteMovieFailure() throws Exception {
        // Given
        Movie getMovie = new Movie(1, "Matrix", "Us", "2020", "30k", "Good movie");

        doReturn(Optional.of(getMovie)).when(service).findById(1);
        doReturn(false).when(service).delete(1);

        // When
        MockHttpServletResponse response =  mockMvc
                .perform(delete("/movie/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    private boolean assertHeaders(Movie movie, MockHttpServletResponse response) {
        boolean etagCheck = true;
        String etagValue = response.getHeader(HttpHeaders.ETAG);

        if(etagValue != null){
            etagCheck = movie.getVersion().toString().equals((etagValue.replace("\"", "")));
        }

        boolean utiCheck = "/movie/{movieId}"
                .replace("\"", "")
                .replace("{movieId}", movie.getId().toString())
                .equals(response.getHeader(HttpHeaders.LOCATION));

        return etagCheck && utiCheck;
    }

    private boolean assertBody(Movie movie, MockHttpServletResponse response) throws UnsupportedEncodingException {
        String actualBody = response.getContentAsString();
        String expected = asJsonString(movie);
        return actualBody.equals(expected);
    }


    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}