package com.innoseti.innosetitestprojec.controller;

import com.innoseti.innosetitestprojec.dto.author.AuthorDto;
import com.innoseti.innosetitestprojec.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @QueryMapping(value = "getAuthors")
    public List<AuthorDto> getAuthors() {
        return authorService.getAuthors();
    }

    @QueryMapping(value = "getAuthorsByNameBook")
    public List<AuthorDto> getAuthorsByNameBook(@Argument("nameBook") String nameBook) {
        return authorService.findAllByNameBook(nameBook);
    }

    @QueryMapping(value = "getAuthorByNameAndLastName")
    public AuthorDto getAuthorByNameAndLastName(@Argument("name") String name, @Argument("lastName") String lastName) {
        return authorService.getAuthorByNameAndLastName(name, lastName);
    }

    @MutationMapping(value = "saveAuthor")
    public String saveAuthor(@Argument("authorRequest") AuthorDto authorDto) {
        return authorService.save(authorDto);
    }

    @MutationMapping(value = "deleteAuthorByNameAndLatsName")
    public String deleteAuthorByNameAndLatsName(@Argument("name") String name, @Argument("lastName") String lastName) {
        return authorService.deleteByNameAndLatsName(name, lastName);
    }

    @MutationMapping(value = "deleteAuthorById")
    public String deleteAuthorById(@Argument("id") String id) {
        return authorService.deleteById(id);
    }

    @MutationMapping(value = "addBookToAuthor")
    public void addBookAuthor(@Argument("authorRequest") AuthorDto authorDto) {
        authorService.addBookToAuthor(authorDto);
    }
}
