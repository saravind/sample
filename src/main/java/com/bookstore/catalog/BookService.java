package com.bookstore.catalog;

import com.bookstore.catalog.common.Book;

import java.util.List;

public interface BookService {

    List<Book> getBooks();

    Book getBookByIsbn(String isbn);

    Book getBook(String id);

}
