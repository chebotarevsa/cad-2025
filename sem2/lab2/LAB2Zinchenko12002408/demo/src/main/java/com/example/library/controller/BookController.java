package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String search, Model model) {
        List<Book> books;
        if (search != null && !search.isBlank()) {
            books = bookService.searchBooks(search);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        return "index";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/";
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = bookService.deleteBook(id);
        if (!deleted) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "❌ Нельзя удалить книгу: она выдана читателю или не найдена.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage",
                    "✅ Книга успешно удалена.");
        }
        return "redirect:/";
    }

    @PostMapping("/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam boolean issued) {
        bookService.updateIssuedStatus(id, issued);
        return "redirect:/";
    }
}