package com.kada.learn.api.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer version = 1;
    private String name;
    private String countryOrigin;
    private String yearOfRelease;
    private String budget;
    private String plot;

    public Movie(){
    }

    public Movie(Integer id) {
        this.id = id;
    }

    public Movie(Integer id, Integer version, String name, String countryOrigin, String releaseDate, String budget, String plot) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.countryOrigin = countryOrigin;
        this.yearOfRelease = releaseDate;
        this.budget = budget;
        this.plot = plot;
    }

    public Movie(Integer version, String name, String countryOrigin, String releaseDate, String budget, String plot) {
        this.version = version;
        this.name = name;
        this.countryOrigin = countryOrigin;
        this.yearOfRelease = releaseDate;
        this.budget = budget;
        this.plot = plot;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryOrigin() {
        return countryOrigin;
    }

    public void setCountryOrigin(String countryOrigin) {
        this.countryOrigin = countryOrigin;
    }

    public String getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(String yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) &&
                Objects.equals(version, movie.version) &&
                Objects.equals(name, movie.name) &&
                Objects.equals(countryOrigin, movie.countryOrigin) &&
                Objects.equals(yearOfRelease, movie.yearOfRelease) &&
                Objects.equals(budget, movie.budget) &&
                Objects.equals(plot, movie.plot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, name, countryOrigin, yearOfRelease, budget, plot);
    }
}
