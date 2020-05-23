package com.akd.app.model;

public class MovieDto {
    private Integer id;
    private Integer version = 1;
    private String name;
    private String countryOrigin;
    private String yearOfRelease;
    private String budget;
    private String plot;

    public MovieDto(){
    }

    public MovieDto(Integer id) {
        this.id = id;
    }

    public MovieDto(Integer id, Integer version, String name, String countryOrigin, String releaseDate, String budget, String plot) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.countryOrigin = countryOrigin;
        this.yearOfRelease = releaseDate;
        this.budget = budget;
        this.plot = plot;
    }

    public MovieDto(Integer version, String name, String countryOrigin, String releaseDate, String budget, String plot) {
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
}
