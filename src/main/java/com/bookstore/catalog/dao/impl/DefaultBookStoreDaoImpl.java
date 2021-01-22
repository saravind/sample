package com.bookstore.catalog.dao.impl;

import com.bookstore.catalog.common.Book;
import com.bookstore.catalog.dao.BookDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Singleton
public class DefaultBookStoreDaoImpl implements BookDao {

    private static final String INSERT_SQL = "INSERT IGNORE into product(id, isbn, bag) values(?, ?, ?)";
    private static final String GET_SQL = "SELECT id, isbn, bag from product where id = ?";
    private static final String GET_ISBN_SQL = "SELECT id, isbn, bag from product where isbn = ?";
    private static final String SELECT_SQL = "SELECT id, isbn, bag from product";


    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;
    private ObjectMapper mapper;


    @Inject
    public DefaultBookStoreDaoImpl(DataSource dataSource, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.mapper = new ObjectMapper();
    }

    @Override
    public void save(@NonNull Book product) throws RuntimeException {
        String value = null;
        try {
            value = mapper.writeValueAsString(product.getAttributeBag());
        } catch (Exception ex) {
            Throwables.propagateIfPossible(ex);
        }
        final String attributeValue = value;
        transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
               return jdbcTemplate.update(INSERT_SQL, product.getId(), product.getIsbn(), attributeValue) ;
            }
        });
    }

    @Override
    public Book getBook(@NonNull String id) {
        List<Book> productList = jdbcTemplate.query(GET_SQL, new Object[]{id}, new BookMapper());
        if (productList == null || productList.isEmpty()) {
            return null;
        } else {
            return productList.get(0);
        }
    }


    @Override
    public Book getBooksOnIsbn(@NonNull String isbn) {
        List<Book> productList = jdbcTemplate.query(GET_ISBN_SQL, new Object[]{isbn}, new BookMapper());
        if (productList == null || productList.isEmpty()) {
            return null;
        } else {
            return productList.get(0);
        }
    }


    @Override
    public List<Book> getBooks() {
        return jdbcTemplate.query(SELECT_SQL, new BookMapper());
    }

    public static class BookMapper implements RowMapper {

        private static ObjectMapper MAPPER = new ObjectMapper();

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String isbn = rs.getString("isbn");
            Map<String, Object> attributesBag = null;

            try {
                attributesBag = (Map<String, Object>)
                        MAPPER.readValue(rs.getString("bag"), Map.class);
            } catch (Exception ex) {
                Throwables.propagateIfPossible(ex);
            }
            return new Book(id, isbn, attributesBag);
        }
    }
}
