package com.example.SpringMVCLibrary.controller;

import com.example.SpringMVCLibrary.model.Author;
import com.example.SpringMVCLibrary.model.Book;
import com.example.SpringMVCLibrary.service.BookService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    private Author author1;
    private Author author2;
    private Book book1;
    private Book book2;
    private List<Book> books;
    private PageRequest pageRequest;
    private Page<Book> booksPage;

    @BeforeEach
    void setup() {
        author1 = new Author(1L, "Name1", "Country1");
        author2 = new Author(2L, "Name2", "Country2");
        book1 = new Book(1L, "Name1", "Language1", author1);
        book2 = new Book(2L, "Name2", "Language2", author2);
        books = Arrays.asList(book1, book2);
        booksPage = new PageImpl<>(books);
        pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));
    }

    @Test
    void shouldBeAbleToReturnListOfBooksWithPaginationAndSorting() throws Exception {
        when(bookService.getAll(pageRequest)).thenReturn(booksPage);

        mockMvc.perform(get("/books?number=0&size=1&sort=name,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$.[0].name").value(books.get(0).getName()))
                .andExpect(jsonPath("$.[0].language").value(books.get(0).getLanguage()))
                .andExpect(jsonPath("$.[0].author").value(books.get(0).getAuthor()))
                .andExpect(jsonPath("$.[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$.[1].name").value(books.get(1).getName()))
                .andExpect(jsonPath("$.[1].language").value(books.get(1).getLanguage()))
                .andExpect(jsonPath("$.[1].author").value(books.get(1).getAuthor()));

        verify(bookService, times(1)).getAll(pageRequest);
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookService.getById(1L)).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(book1.getId()))
                .andExpect(jsonPath("$.name").value(book1.getName()))
                .andExpect(jsonPath("$.language").value(book1.getLanguage()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()));

        verify(bookService, times(1)).getById(1L);
    }

    @Test
    void createTest() throws Exception {
        when(bookService.create(book1)).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .content(getMapper().writeValueAsString(book1))
                        .contentType("application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(book1.getId()))
                .andExpect(jsonPath("$.name").value(book1.getName()))
                .andExpect(jsonPath("$.language").value(book1.getLanguage()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()));

        verify(bookService, times(1)).create(book1);
    }

    @Test
    void updateTest() throws Exception {
        when(bookService.update(book1, 1L)).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders.put("/book/1")
                        .content(getMapper().writeValueAsString(book1))
                        .contentType("application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(book1.getId()))
                .andExpect(jsonPath("$.name").value(book1.getName()))
                .andExpect(jsonPath("$.language").value(book1.getLanguage()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()));

        verify(bookService, times(1)).update(book1, 1L);
    }

    @Test
    void deleteByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/1"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteById(1L);
    }

    private ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        return objectMapper;
    }
}
