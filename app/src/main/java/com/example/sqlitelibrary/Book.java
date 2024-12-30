package com.example.sqlitelibrary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String bookName;
    public String author;

    public Book(String bookName, String author) {
        this.bookName = bookName;
        this.author = author;
    }

    public String getBook() {
        return "Author: "+author + " \nbook: " + bookName;
    }
}
