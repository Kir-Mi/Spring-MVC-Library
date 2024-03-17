package com.example.SpringMVCLibrary.service;

import com.example.SpringMVCLibrary.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<Book> getAll(Pageable pageable);
    Book getById(Long id);
    Book create(Book book);
    Book update(Book book, Long id);
    void deleteById(Long id);
}
