package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void deleteBook(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    public Book findById(Long id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>(books);
        }
        String lowerQuery = query.toLowerCase();
        return books.stream()
                .filter(book ->
                        book.getTitle().toLowerCase().contains(lowerQuery) ||
                                book.getAuthor().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}