package com.bookstore.catalog;

import com.bookstore.catalog.common.Book;
import com.bookstore.catalog.dao.BookDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.NonNull;

import java.util.List;

@Singleton
public class DefaultBookServiceImpl implements BookService {

    private BookDao bookDao;

    @Inject
    public DefaultBookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book> getBooks() {
        return bookDao.getBooks();
    }

    @Override
    public Book getBookByIsbn(@NonNull String isbn) {
        return bookDao.getBooksOnIsbn(isbn);
    }

    @Override
    public Book getBook(String id) {
        return bookDao.getBook(id);
    }
}
