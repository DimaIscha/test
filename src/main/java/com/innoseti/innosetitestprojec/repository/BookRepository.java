package com.innoseti.innosetitestprojec.repository;

import com.innoseti.innosetitestprojec.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("select book from Book book where book.name = :name")
    Optional<Book> findByName(@Param("name") String name);

    @Query("select book from Book book")
    List<Book> findAll();

    @Query("select book from Book book where book.name = :name")
    Optional<Book> getBookByName(@Param("name") String name);

    @Query("select book from Book book join fetch book.authors authors where authors.name = :name and authors.lastName = :lastName")
    List<Book> getBooksByAuthorNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);

    @Modifying
    @Query("delete from Book book where book.name in :names")
    void deleteBooksByNames(@Param("names") List<String> names);
}
