package com.innoseti.innosetitestprojec.service;

import com.innoseti.innosetitestprojec.dto.book.BookDto;
import com.innoseti.innosetitestprojec.exception.ExistsInDataBaseException;
import com.innoseti.innosetitestprojec.exception.NotFoundException;
import com.innoseti.innosetitestprojec.mapstruct.AuthorMapper;
import com.innoseti.innosetitestprojec.mapstruct.BookMapper;
import com.innoseti.innosetitestprojec.repository.AuthorRepository;
import com.innoseti.innosetitestprojec.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    public List<BookDto> getBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::bookModelToBookDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDto getBookByName(String name) {
        var book = bookRepository
                .getBookByName(name)
                .orElseThrow(() -> new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Book] [name: %S]", name))));
        return bookMapper.bookModelToBookDto(book);
    }

    @Transactional(readOnly = true)
    public List<BookDto> getBooksByAuthorNameAndLastName(String name, String lastName) {
        var books = bookRepository.getBooksByAuthorNameAndLastName(name, lastName);

        return books
                .stream()
                .map(bookMapper::bookModelToBookDto)
                .toList();
    }

    @Transactional
    public String deleteBookByName(String name) {
        var bookInDataBase = bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Book] [name: %s]", name))));

        bookInDataBase.getAuthors().forEach(author -> author.getBooks().removeIf(book -> book.equals(bookInDataBase)));

        bookRepository.delete(bookInDataBase);
        return bookInDataBase.getId();
    }

    @Transactional
    public String deleteBookById(String id) {
        var bookInDataBase = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Book] [id: %s]", id))));

        bookInDataBase.getAuthors().forEach(author -> author.getBooks().removeIf(book -> book.equals(bookInDataBase)));

        bookRepository.delete(bookInDataBase);
        return bookInDataBase.getId();
    }
    @Transactional
    public String saveBook(BookDto bookDto) {
        var bookModelOptional = bookRepository.findByName(bookDto.getName());

        if (bookModelOptional.isPresent()) {
            throw new ExistsInDataBaseException(ExistsInDataBaseException.getCustomMessage(String.format("[object: Book] [name: %s]", bookDto.getName())));
        }

        var authorDtoLinks = bookDto.getAuthors().stream().distinct().toList();

        var authorsExistInDataBase = authorDtoLinks
                .stream()
                .map(authorDtoLink -> authorRepository.findByNameAndLastName(authorDtoLink.getName(), authorDtoLink.getLastName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (authorsExistInDataBase.size() == authorDtoLinks.size()) {
            var bookModelCandidateInSave = bookMapper.bookDtoToBookModel(bookDto);
            authorsExistInDataBase.forEach(author -> author.addBook(bookModelCandidateInSave));
            return bookRepository.save(bookModelCandidateInSave).getId();
        } else {
            List<String> names = new ArrayList<>();
            List<String> lastnames = new ArrayList<>();

            authorDtoLinks
                    .stream()
                    .map(authorMapper::authorLinkDtoToAuthorModel)
                    .filter(Predicate.not(authorsExistInDataBase::contains))
                    .forEach(author -> {
                        names.add(author.getName());
                        lastnames.add(author.getLastName());
                    });

            throw new NotFoundException(NotFoundException.getCustomMessage(String.format("[object: Author] [name: %s] [lastName: %s]", names, lastnames)));
        }
    }
}
