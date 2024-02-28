package com.innoseti.innosetitestprojec.repository;

import com.innoseti.innosetitestprojec.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    @Query("select author from Author author join fetch author.books book where book.name = :nameBook")
    List<Author> findAllByNameBook(@Param("nameBook") String nameBook);

    @Query("select author from Author author where author.id = :id")
    Optional<Author> findById(@Param("id") String id);

    @Query("select author from Author author where author.name = :name and author.lastName = :lastName")
    Optional<Author> findByNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);

    @Modifying
    @Query("delete from Author author where author.name = :name and author.lastName = :lastName")
    void deleteByNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);
}
