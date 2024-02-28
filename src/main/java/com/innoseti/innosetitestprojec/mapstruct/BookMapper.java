package com.innoseti.innosetitestprojec.mapstruct;

import com.innoseti.innosetitestprojec.dto.book.BookDto;
import com.innoseti.innosetitestprojec.dto.book.BookDtoLink;
import com.innoseti.innosetitestprojec.model.Book;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder, uses = AuthorMapper.class)
public interface BookMapper {
    BookDto bookModelToBookDto(Book book);

    Book bookDtoToBookModel(BookDto bookDto);
}

