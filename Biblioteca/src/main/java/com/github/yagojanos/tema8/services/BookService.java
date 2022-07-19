package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Book;
import com.github.yagojanos.tema8.dao.BookDAO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookService {

    private BookDAO bookDAO;

    public BookService(BookDAO bookDAO){
        this.bookDAO = bookDAO;
    }

    public boolean registerNewBook(Book book){

        if(book == null){
            throw new RuntimeException("Null book");
        }

        if(bookDAO.findAll().stream().anyMatch(p -> p.getId() == book.getId())) {
            throw new RuntimeException("Book already exists");
        }

        return bookDAO.save(book);
    }

    public boolean deleteBook(int id) {

        List<Book> allBooksList = bookDAO.findAll();

        if(allBooksList.size() == 0){
            throw new RuntimeException("No books in the list");
        }

        Book book = allBooksList.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        if(book == null){
            throw new RuntimeException("This book does not exist to be deleted");
        }

        if(book.isWithSomeone() == true){
            throw new RuntimeException("Can not delete this book because it's with someone");
        }

        return bookDAO.delete(id);
    }

    public boolean updateBook(Book book){

        if(book == null){
            throw new RuntimeException("You can not save a client whose instance does not exist");
        }

        if(bookDAO.findAll().stream().anyMatch(p -> p.getId() == book.getId())) {
            return bookDAO.update(book);
        }  else {
            throw new RuntimeException("Try to create a new book, this book does not exist");
        }
    }

    public Optional<Book> searchBookById(int id){
        return bookDAO.findById(id);
    }

    public List<Book> searchBookByAuthor(String author){

        List<Book> booksOfThisAuthor = bookDAO.findByAuthor(author);

        if(booksOfThisAuthor.size() == 0){

            System.out.println("No books found");
            return Collections.EMPTY_LIST;
        } else {
            return booksOfThisAuthor;
        }
    }

    public List<Book> listAllBooks(){
        return bookDAO.findAll();
    }

}
