package com.rodsussumu.experian.repositories;

import com.rodsussumu.experian.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}
