package com.bookstore.driver;

import com.bookstore.catalog.BookService;
import com.bookstore.catalog.common.Book;
import com.bookstore.catalog.dao.BookDao;
import com.bookstore.catalog.dao.config.JdbcConfigurationConfig;
import com.bookstore.catalog.module.BookCatalogModule;
import com.bookstore.catalog.module.JdbcModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MainDriver {

    public static void main(String[] args) throws IOException, SQLException {
        JdbcConfigurationConfig config = new JdbcConfigurationConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setUrl("jdbc:h2:mem:testDb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");


        Injector injector = Guice.createInjector(new JdbcModule(config), new BookCatalogModule());

        DataSource dataSource = injector.getInstance(DataSource.class);
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("DROP table product if EXISTS ");

            statement.execute("CREATE TABLE product ( id varchar(63) NOT NULL, " +
                    "isbn VARCHAR(63) NOT NULL, bag VARCHAR(10248) NOT NULL, PRIMARY KEY (id) )");
            statement.execute("CREATE INDEX isbn_idx on product(isbn)");

        }

        Map<String, Object> value = new HashMap<>();
        value.put("a", "1");
        value.put("b", "2");
        value.put("c", "3");
        value.put("d", "4");

        System.out.println("Use DAO to ingest the attributes");
        BookDao bookDao = injector.getInstance(BookDao.class);

        bookDao.save(new Book("1", "1isbn", value));
        bookDao.save(new Book("2", "2isbn", value));
        bookDao.save(new Book("3", "4isbn", value));
        bookDao.save(new Book("4", "6isbn", value));

        System.out.println(bookDao.getBook("1"));
        System.out.println(bookDao.getBooksOnIsbn("6isbn"));
        System.out.println(bookDao.getBooks());

        System.out.println("====================");
        BookService bookService = injector.getInstance(BookService.class);
        System.out.println(bookService.getBook("1"));
        System.out.println(bookService.getBookByIsbn("6isbn"));
        System.out.println(bookService.getBooks());


    }

}
