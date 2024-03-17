package com.example.SpringMVCLibrary.service;

import com.example.SpringMVCLibrary.exception.NotFoundException;
import com.example.SpringMVCLibrary.model.Book;
import com.example.SpringMVCLibrary.repository.AuthorRepository;
import com.example.SpringMVCLibrary.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public Page<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Book create(Book book) {
        authorRepository.save(book.getAuthor());
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book, Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found", HttpStatus.NOT_FOUND));
        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setLanguage(book.getLanguage());
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
