package com.github.yagojanos.tema8.entities;

import com.github.yagojanos.tema8.services.BookService;
import com.github.yagojanos.tema8.dao.BookDAO;

import java.util.Comparator;

public class Book {

    private BookService bookService = new BookService(new BookDAO());

    private int id;
    private String title;
    private String author;
    private boolean withSomeone;
    private int customerWithThisBookId;

    private static int idCounter;


    public Book(String title, String author){
        updateId();
        this.title = title;
        this.author = author;
    }

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isWithSomeone(){
        return withSomeone;
    }

    public void setWithSomeone(boolean withSomeone){
        this.withSomeone = withSomeone;
    }

    public int getCustomerWithThisBookId() {
        return customerWithThisBookId;
    }

    public void setCustomerWithThisBookId(int customerWithThisBookId) {
        this.customerWithThisBookId = customerWithThisBookId;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void updateId() {

        if(bookService.listAllBooks().size() > 0){

            idCounter = bookService.listAllBooks().stream()
                    .max(Comparator.comparing(Book::getId))
                    .get()
                    .getId();
        } else {
            idCounter = 0;
        }

        this.id = ++idCounter;
    }

    @Override
    public String toString() {
        return  "Book id = " + id +
                ", title = " + title +
                ", author = " + author +
                ", the book is with someone = " + withSomeone +
                ", customerWithThisBookId = " + customerWithThisBookId;
    }
}
