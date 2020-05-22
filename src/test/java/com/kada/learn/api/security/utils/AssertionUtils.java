package com.kada.learn.api.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kada.learn.api.security.model.Movie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class AssertionUtils {
    public static boolean assertHeaders(Movie movie, ResponseEntity<Movie> response) {

        boolean etagCheck = true;
        String etagValue = response.getHeaders().getETag();

        if(etagValue != null){
            etagCheck = movie.getVersion().toString().equals((etagValue.replace("\"", "")));
        }

        String actualLocationValue = Objects.requireNonNull(response.getHeaders().getLocation()).toString();
        String expectedLocationValue = "/movie/{movieId}"
                .replace("{movieId}", movie.getId().toString());

        boolean utiCheck = actualLocationValue.equals(expectedLocationValue);

        return etagCheck && utiCheck;
    }

    public static boolean assertHeaders(Movie movie, MockHttpServletResponse response) {

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

    public static boolean assertBody(Movie movie, MockHttpServletResponse response) throws UnsupportedEncodingException {
        String actualBody = response.getContentAsString();
        String expected = asJsonString(movie);
        return actualBody.equals(expected);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
