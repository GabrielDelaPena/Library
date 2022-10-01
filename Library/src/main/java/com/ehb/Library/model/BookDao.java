package com.ehb.Library.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookDao extends CrudRepository<Book, Integer> {

    @Query("select b from Book b where b.title like %:keyword%")
    public List<Book> filteredBooks(String keyword);

}
