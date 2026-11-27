package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        bookRepository.addBook(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public boolean deleteBook(Long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            return false;
        }
        if (book.isIssued()) {
            return false;
        }
        bookRepository.deleteBook(id);
        return true;
    }

    public void updateIssuedStatus(Long id, boolean issued) {
        Book book = bookRepository.findById(id);
        if (book != null) {
            book.setIssued(issued);
        }
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.searchBooks(query);
    }
}