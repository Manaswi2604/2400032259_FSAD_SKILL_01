package com.klef.fsad.exam;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Movie Entity Class - Mapped to 'movies' table in fsadexam database
 */
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private int id;

    @Column(name = "movie_name", nullable = false, length = 100)
    private String name;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "genre", length = 50)
    private String genre;

    @Column(name = "director", length = 100)
    private String director;

    @Column(name = "rating")
    private double rating;

    // ──────────────────────────────────────────────────
    // Constructors
    // ──────────────────────────────────────────────────

    public Movie() {}

    public Movie(String name, LocalDate releaseDate, String status,
                 String genre, String director, double rating) {
        this.name        = name;
        this.releaseDate = releaseDate;
        this.status      = status;
        this.genre       = genre;
        this.director    = director;
        this.rating      = rating;
    }

    // ──────────────────────────────────────────────────
    // Getters & Setters
    // ──────────────────────────────────────────────────

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public String getName()              { return name; }
    public void setName(String name)     { this.name = name; }

    public LocalDate getReleaseDate()               { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getStatus()            { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGenre()             { return genre; }
    public void setGenre(String genre)   { this.genre = genre; }

    public String getDirector()          { return director; }
    public void setDirector(String d)    { this.director = d; }

    public double getRating()            { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    // ──────────────────────────────────────────────────
    // toString
    // ──────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Movie [id=" + id
             + ", name=" + name
             + ", releaseDate=" + releaseDate
             + ", status=" + status
             + ", genre=" + genre
             + ", director=" + director
             + ", rating=" + rating + "]";
    }
}
