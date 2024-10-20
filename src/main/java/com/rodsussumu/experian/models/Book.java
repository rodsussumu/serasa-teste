package com.rodsussumu.experian.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String title;

    private String genre;

    @Column(name = "release_year")
    private String releaseYear;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
