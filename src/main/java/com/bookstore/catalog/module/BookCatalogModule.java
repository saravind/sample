package com.bookstore.catalog.module;

import com.bookstore.catalog.BookService;
import com.bookstore.catalog.DefaultBookServiceImpl;
import com.google.inject.AbstractModule;

public class BookCatalogModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BookService.class).to(DefaultBookServiceImpl.class);
    }

}
