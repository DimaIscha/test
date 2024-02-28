package com.innoseti.innosetitestprojec.mapstruct;

import com.innoseti.innosetitestprojec.dto.author.AuthorDto;
import com.innoseti.innosetitestprojec.dto.author.AuthorDtoLink;
import com.innoseti.innosetitestprojec.dto.book.BookDtoLink;
import com.innoseti.innosetitestprojec.model.Author;
import com.innoseti.innosetitestprojec.model.Book;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder, uses = BookMapper.class)
public interface AuthorMapper {

    AuthorDto authorModelToAuthorDto(Author author);

    Author authorDtoToAuthorModel(AuthorDto authorDto);

    Author authorLinkDtoToAuthorModel(AuthorDtoLink authorDtoLink);
}
