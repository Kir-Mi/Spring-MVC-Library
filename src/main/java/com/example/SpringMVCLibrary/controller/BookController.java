package com.example.SpringMVCLibrary.controller;

import com.example.SpringMVCLibrary.model.Book;
import com.example.SpringMVCLibrary.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @GetMapping("/books")
    public List<Book> getAll(Pageable pageable) {
        return service.getAll(pageable).getContent();
    }

    @GetMapping("/book/{book_id}")
    public Book getById(@PathVariable(value = "book_id") Long id) {
        return service.getById(id);
    }

    @PostMapping("/book")
    public Book create(@Valid @RequestBody Book book) {
        return service.create(book);
    }

    @PutMapping("/book/{book_id}")
    public Book update(@Valid @RequestBody Book book, @PathVariable(value = "book_id") Long id) {
        return service.update(book, id);
    }

    @DeleteMapping("/book/{book_id}")
    public void deleteById(@PathVariable(value = "book_id") Long id) {
        service.deleteById(id);
    }
}
