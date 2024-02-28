package com.innoseti.innosetitestprojec.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "author")
@ToString(exclude = "books")
@EqualsAndHashCode(exclude = {"books"})
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.innoseti.innosetitestprojec.model.CustomUUIDGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @JoinTable(name = "authors_books", joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Fetch(FetchMode.SUBSELECT)
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
        book.getAuthors().add(this);
    }
}
