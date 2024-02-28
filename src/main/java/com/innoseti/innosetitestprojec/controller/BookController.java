package com.innoseti.innosetitestprojec.controller;

import com.innoseti.innosetitestprojec.dto.book.BookDto;
import com.innoseti.innosetitestprojec.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @QueryMapping(value = "getBooks")
    public List<BookDto> getBooks() {
        return bookService.getBooks();
    }

    @QueryMapping(value = "getBookByName")
    public BookDto getBookByName(@Argument("name") String name) {
        return bookService.getBookByName(name);
    }

    @QueryMapping(value = "getBooksByAuthorNameAndLastName")
    public List<BookDto> getBooksByAuthorNameAndLastName(@Argument("name") String name, @Argument("lastName") String lastName) {
        return bookService.getBooksByAuthorNameAndLastName(name, lastName);
    }

    @MutationMapping(value = "saveBook")
    public String saveBook(@Argument("bookRequest") BookDto bookDto) {
        return bookService.saveBook(bookDto);
    }

    @MutationMapping(value = "deleteBookByName")
    public String deleteBookByName(@Argument("name") String name) {
        return bookService.deleteBookByName(name);
    }

    @MutationMapping(value = "deleteBookById")
    public String deleteBookById(@Argument("id") String id) {
        return bookService.deleteBookById(id);
    }
}
