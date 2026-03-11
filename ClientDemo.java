package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * ClientDemo - Demonstrates Hibernate HQL operations on the Movie entity.
 *
 * Operations covered:
 *   I.  Insert records using a persistent object (save via Session)
 *   II. Update Name and Status by ID using HQL with positional parameters
 */
public class ClientDemo {

    // ─────────────────────────────────────────────────────────────
    // Build SessionFactory once for the whole application
    // ─────────────────────────────────────────────────────────────
    private static SessionFactory buildSessionFactory() {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    // ─────────────────────────────────────────────────────────────
    // I. INSERT – persist Movie objects using Session.save()
    // ─────────────────────────────────────────────────────────────
    public static void insertMovies(SessionFactory factory) {
        System.out.println("\n========== I. INSERTING MOVIE RECORDS ==========");

        Session session = factory.openSession();
        Transaction tx  = null;

        try {
            tx = session.beginTransaction();

            // Create persistent Movie objects
            Movie m1 = new Movie("Interstellar",
                                 LocalDate.of(2014, 11, 7),
                                 "Released", "Sci-Fi", "Christopher Nolan", 8.6);

            Movie m2 = new Movie("Inception",
                                 LocalDate.of(2010, 7, 16),
                                 "Released", "Thriller", "Christopher Nolan", 8.8);

            Movie m3 = new Movie("Dune: Part Two",
                                 LocalDate.of(2024, 3, 1),
                                 "Released", "Sci-Fi", "Denis Villeneuve", 8.5);

            Movie m4 = new Movie("Avatar 3",
                                 LocalDate.of(2025, 12, 19),
                                 "Upcoming", "Action", "James Cameron", 0.0);

            Movie m5 = new Movie("The Batman",
                                 LocalDate.of(2022, 3, 4),
                                 "Released", "Action", "Matt Reeves", 7.8);

            // Persist each object
            session.save(m1);
            session.save(m2);
            session.save(m3);
            session.save(m4);
            session.save(m5);

            tx.commit();
            System.out.println("✔ 5 movie records inserted successfully.");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Insert failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // II. UPDATE – Name and Status by ID using HQL positional params
    // ─────────────────────────────────────────────────────────────
    public static void updateMovieByHQL(SessionFactory factory,
                                        int movieId,
                                        String newName,
                                        String newStatus) {
        System.out.println("\n========== II. UPDATING MOVIE (HQL – Positional Parameters) ==========");
        System.out.println("Updating Movie ID = " + movieId
                         + "  →  Name: \"" + newName
                         + "\"  |  Status: \"" + newStatus + "\"");

        Session session = factory.openSession();
        Transaction tx  = null;

        try {
            tx = session.beginTransaction();

            /*
             * HQL UPDATE with positional parameters (?1, ?2, ?3)
             * ?1 → newName
             * ?2 → newStatus
             * ?3 → movieId
             */
            String hql = "UPDATE Movie SET name = ?1, status = ?2 WHERE id = ?3";

            Query<?> query = session.createQuery(hql);
            query.setParameter(1, newName);
            query.setParameter(2, newStatus);
            query.setParameter(3, movieId);

            int rowsAffected = query.executeUpdate();
            tx.commit();

            if (rowsAffected > 0) {
                System.out.println("✔ Update successful. Rows affected: " + rowsAffected);
            } else {
                System.out.println("⚠ No record found with ID = " + movieId);
            }

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Update failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // DISPLAY ALL – HQL SELECT to verify current state of table
    // ─────────────────────────────────────────────────────────────
    public static void displayAllMovies(SessionFactory factory) {
        System.out.println("\n========== CURRENT MOVIE RECORDS ==========");

        Session session = factory.openSession();
        try {
            String hql = "FROM Movie ORDER BY id";
            Query<Movie> query = session.createQuery(hql, Movie.class);
            List<Movie> movies = query.getResultList();

            if (movies.isEmpty()) {
                System.out.println("No records found.");
            } else {
                movies.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Display failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN – entry point
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        SessionFactory factory = buildSessionFactory();

        try {
            // ── I. Insert records ──────────────────────────────
            insertMovies(factory);

            // Show records after insert
            displayAllMovies(factory);

            // ── II. Update records using HQL positional params ─
            // Update Movie ID=1: rename and mark as "Archived"
            updateMovieByHQL(factory, 1, "Interstellar (Director's Cut)", "Archived");

            // Update Movie ID=4: Avatar 3 status changed to "Released"
            updateMovieByHQL(factory, 4, "Avatar: Fire and Ash", "Released");

            // Show final state
            displayAllMovies(factory);

        } finally {
            factory.close();
            System.out.println("\n✔ SessionFactory closed. Program complete.");
        }
    }
}
