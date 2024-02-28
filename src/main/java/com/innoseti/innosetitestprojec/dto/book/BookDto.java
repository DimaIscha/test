package com.innoseti.innosetitestprojec.dto.book;

import com.innoseti.innosetitestprojec.dto.author.AuthorDtoLink;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "authors")
public class BookDto {
    private String id;
    private String name;
    private List<AuthorDtoLink> authors = new ArrayList<>();
}
