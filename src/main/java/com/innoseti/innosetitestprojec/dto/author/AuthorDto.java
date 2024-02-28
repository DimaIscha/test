package com.innoseti.innosetitestprojec.dto.author;

import com.innoseti.innosetitestprojec.dto.book.BookDtoLink;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "books")
public class AuthorDto {
    private String id;
    private String name;
    private String lastName;
    private List<BookDtoLink> books = new ArrayList<>();
}
