package com.bookstore.catalog.dao;

import com.bookstore.catalog.common.Book;

import java.util.List;

public interface BookDao {

    void save(Book product);

    Book getBook(String id);

    Book getBooksOnIsbn(String isbn);

    List<Book> getBooks();
}
