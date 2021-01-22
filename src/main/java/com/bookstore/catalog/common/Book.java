package com.bookstore.catalog.common;


import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String id;
    private String isbn;
    // This could be attributes to be stored like author, price etc
    private Map<String, Object> attributeBag;

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", isbn='" + isbn + '\'' +
                ", attributeBag=" + attributeBag.keySet() + "::" + attributeBag.values() +
                '}';
    }
}
