package com.innoseti.innosetitestprojec.service;


import com.innoseti.innosetitestprojec.dto.author.AuthorDto;
import com.innoseti.innosetitestprojec.dto.book.BookDtoLink;
import com.innoseti.innosetitestprojec.exception.ExistsInDataBaseException;
import com.innoseti.innosetitestprojec.exception.NotFoundException;
import com.innoseti.innosetitestprojec.mapstruct.AuthorMapper;
import com.innoseti.innosetitestprojec.mapstruct.BookMapper;
import com.innoseti.innosetitestprojec.model.Author;
import com.innoseti.innosetitestprojec.model.Book;
import com.innoseti.innosetitestprojec.repository.AuthorRepository;
import com.innoseti.innosetitestprojec.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorMapper authorMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<AuthorDto> getAuthors() {
        return authorRepository
                .findAll()
                .stream()
                .map(authorMapper::authorModelToAuthorDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AuthorDto getAuthorByNameAndLastName(String name, String lastName) {
        var authorModel = authorRepository.findByNameAndLastName(name, lastName)
                .orElseThrow(() -> new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Author] [name: %s] [lastName: %s]", name, lastName))));
        return authorMapper.authorModelToAuthorDto(authorModel);
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> findAllByNameBook(String nameBook) {
        var authors = authorRepository.findAllByNameBook(nameBook);

        return authors
                .stream()
                .map(authorMapper::authorModelToAuthorDto)
                .toList();
    }

    @Transactional
    public String save(AuthorDto authorDto) {
        checkNotExistAuthorInDataBase(authorDto.getName(), authorDto.getLastName());
        checkNotExistBookInDataBase(authorDto.getBooks());

        var author = authorMapper.authorDtoToAuthorModel(authorDto);
        return authorRepository.save(author).getId();
    }

    @Transactional
    public String deleteByNameAndLatsName(String name, String latName) {
        Author author = checkExistAuthorInDataBase(name, latName);
        authorRepository.deleteByNameAndLastName(name, latName);
        bookRepository.deleteBooksByNames(candidateBookToDelete(author));
        return author.getId();
    }

    @Transactional
    public String deleteById(String id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Author] [id: %s]]", id))));
        authorRepository.deleteById(id);
        bookRepository.deleteBooksByNames(candidateBookToDelete(author));
        return author.getId();
    }

    @Transactional
    public void addBookToAuthor(AuthorDto authorDto) {
        var authorModel = authorRepository.findByNameAndLastName(authorDto.getName(), authorDto.getLastName())
                .orElseThrow(() -> new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Author] [name: %s] [lastName: %s]", authorDto.getName(), authorDto.getLastName()))));

        var bookDtoLinks = authorDto.getBooks().stream().distinct().toList();

        var booksModelInDataBase = bookDtoLinks
                .stream()
                .map(bookDtoLink -> bookRepository.findByName(bookDtoLink.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();


        var namesBooksHasAuthor = booksModelInDataBase.
                stream()
                .filter(book -> authorModel.getBooks().contains(book))
                .map(Book::getName)
                .collect(Collectors.joining(", "));

        if (!namesBooksHasAuthor.isEmpty()) {
            throw new ExistsInDataBaseException(ExistsInDataBaseException.getCustomMessage(String.format("[object: Author] [уже существующие книги у автора: %s]", namesBooksHasAuthor)));
        }


        if (bookDtoLinks.size() == booksModelInDataBase.size()) {
            booksModelInDataBase.forEach(authorModel::addBook);
            authorRepository.save(authorModel);
        } else {
            List<String> namesBooksNotInDataBase = bookDtoLinks
                    .stream()
                    .distinct()
                    .map(BookDtoLink::getName)
                    .map(nameBook -> Book.builder().name(nameBook).build())
                    .filter(Predicate.not(booksModelInDataBase::contains))
                    .map(Book::getName)
                    .toList();

            throw new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Book] [names: %s]", namesBooksNotInDataBase)));
        }
    }

    private void checkNotExistBookInDataBase(List<BookDtoLink> bookDtoLinks) {
        bookDtoLinks
                .stream()
                .map(bookDtoLink -> bookRepository.findByName(bookDtoLink.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .ifPresent(book -> {
                    throw new ExistsInDataBaseException(ExistsInDataBaseException.getCustomMessage(String.format("[object: Book] [name: %s]", book.getName())));
                });
    }

    private void checkNotExistAuthorInDataBase(String name, String lastName) {
        authorRepository.findByNameAndLastName(name, lastName)
                .ifPresent(author -> {
                    throw new ExistsInDataBaseException(ExistsInDataBaseException.getCustomMessage(String.format("[object: Author] [name: %s] [lastName: %s]", author.getName(), author.getLastName())));
                });
    }

    private Author checkExistAuthorInDataBase(String name, String lastName) {
        return authorRepository.findByNameAndLastName(name, lastName)
                .orElseThrow(() -> new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Author] [name: %s] [lastName: %s]", name, lastName))));
    }

    private List<String> candidateBookToDelete(Author authorModel) {
        List<String> nameBook = new ArrayList<>();

        for (Book book : authorModel.getBooks()) {
            Optional<Author> isAuthorNotEqualsThisAuthor = book
                    .getAuthors()
                    .stream()
                    .filter(author -> !author.getName().equals(authorModel.getName()) && !author.getLastName().equals(authorModel.getLastName()))
                    .findFirst();

            if (isAuthorNotEqualsThisAuthor.isEmpty()) {
                nameBook.add(book.getName());
            }
        }
        return nameBook;
    }
}
