package com.kada.learn.api.security.web;

import com.kada.learn.api.security.exception.AlreadyExistException;
import com.kada.learn.api.security.model.Movie;
import com.kada.learn.api.security.model.MovieDto;
import com.kada.learn.api.security.service.MovieService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class MovieController {

    private static final Logger logger = LogManager.getLogger(MovieController.class);

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<?> getMovie(@PathVariable Integer id) {
        return service.findById(id)
                .map(movie -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(movie.getVersion()))
                                .location(new URI("/movie/" + movie.getId()))
                                .body(movie);
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/movies")
    public ResponseEntity<?> getMovies() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @PostMapping("/movie")
    public ResponseEntity<Movie> createMovie(@RequestBody MovieDto movieDto) {

        ModelMapper modelMapper = new ModelMapper();
        Movie movie = modelMapper.map(movieDto, Movie.class);

        // make sure the movie does not exist already
        if(service.findByName(movie.getName()).orElse(null) != null){
            throw new AlreadyExistException("The movie you are trying to create already exists");
        }

        // Save the movie to the database
        Movie newMovie = service.save(movie);
        logger.info("Saved movie: {}", newMovie);

        try {
            // Build a created response
            return ResponseEntity
                    .created(new URI("/movie/" + newMovie.getId()))
                    .eTag(Integer.toString(newMovie.getVersion()))
                    .body(newMovie);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/movie/{id}")
    public ResponseEntity<Movie> updateMovie(
            @RequestBody MovieDto movieDto,
            @PathVariable Integer id,
            @RequestHeader("If-Match") Integer ifMatch) {

        ModelMapper modelMapper = new ModelMapper();
        Movie movie = modelMapper.map(movieDto, Movie.class);

        logger.info("Updating movie with id: {}, {}", movie.getId(), movie);

        // Get the existing movie
        Optional<Movie> existingMovie = service.findById(id);

        Optional<ResponseEntity<Movie>> responseEntity = existingMovie.map(m -> {
            logger.info("Movie with ID: " + id + " has a version of " + m.getVersion());
            logger.info("Update is for IF-Match " + ifMatch);
            if (!m.getVersion().equals(ifMatch)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Update the movie
            if (movie.getCountryOrigin() != null) {
                m.setCountryOrigin(movie.getCountryOrigin());
            }
            if (movie.getYearOfRelease() != null) {
                m.setYearOfRelease(movie.getYearOfRelease());
            }
            if (movie.getBudget() != null) {
                m.setBudget(movie.getBudget());
            }
            if (movie.getPlot() != null) {
                m.setPlot(movie.getPlot());
            }
            try {
                // Perform update and return OK
                if (service.update(m)) {
                    return ResponseEntity
                            .ok()
                            .location(new URI("/movie/" + m.getId()))
                            .eTag(Integer.toString(m.getVersion()))
                            .body(m);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });

        return responseEntity.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Integer id) {

        logger.info("Deleting movie with ID {}", id);

        // Get the existing product
        Optional<Movie> existingMovie = service.findById(id);

        // Delete the review if it exists in the database
        return existingMovie.map(movie -> {
            if(service.delete(id)){
                return ResponseEntity.status(HttpStatus.OK).build();
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
