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
@Table(name = "book")
@ToString(exclude = "authors")
@EqualsAndHashCode(exclude = {"authors"})
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.innoseti.innosetitestprojec.model.CustomUUIDGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(mappedBy = "books")
    private List<Author> authors = new ArrayList<>();
}
