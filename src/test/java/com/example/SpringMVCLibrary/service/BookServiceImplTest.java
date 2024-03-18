package com.example.SpringMVCLibrary.service;

import com.example.SpringMVCLibrary.exception.NotFoundException;
import com.example.SpringMVCLibrary.model.Author;
import com.example.SpringMVCLibrary.model.Book;
import com.example.SpringMVCLibrary.repository.AuthorRepository;
import com.example.SpringMVCLibrary.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @Mock
    private BookRepository repository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private BookServiceImpl service;

    private Author author1;
    private Author author2;
    private Book book1;
    private Book book2;
    private PageRequest pageRequest;
    private Page<Book> booksPage;
    private List<Book> books;

    @BeforeEach
    void setup() {
        author1 = new Author(1L, "Name1", "Country1");
        author2 = new Author(2L, "Name2", "Country2");
        book1 = new Book(1L, "Name1", "Language1", author1);
        book2 = new Book(2L, "Name2", "Language2", author2);
        books = Arrays.asList(book1, book2);
        pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));
        booksPage = new PageImpl<>(books);
    }

    @Test
    void getAllTest() {
        when(repository.findAll(pageRequest)).thenReturn(booksPage);

        assertEquals(booksPage, service.getAll(pageRequest));

        verify(repository, times(1)).findAll(pageRequest);
    }

    @Test
    void getByIdTest() {
        when(repository.findById(book1.getId())).thenReturn(Optional.ofNullable(book1));

        assertEquals(book1, service.getById(book1.getId()));

        verify(repository, times(1)).findById(book1.getId());
    }

    @Test
    void whenBookNotFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(1L));

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void createTest() {
        when(repository.save(book1)).thenReturn(book1);
        when(authorRepository.save(author1)).thenReturn(author1);


        assertEquals(book1, service.create(book1));

        verify(repository, times(1)).save(book1);
        verify(authorRepository, times(1)).save(author1);
    }

    @Test
    void updateTest() {
        Book existingBook = new Book(1L, "Name1", "language1", null);
        Book updatedBook = new Book(1L, "Name2", "language2", null);
        when(repository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(repository.save(updatedBook)).thenReturn(updatedBook);

        assertEquals(updatedBook, service.update(updatedBook, 1L));
    }

    @Test
    void updateBook_nonFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(book1, 1L));

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deleteByIdTest() {
        doNothing().when(repository).deleteById(1L);

        service.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
