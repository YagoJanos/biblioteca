package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Book;
import com.github.yagojanos.tema8.dao.BookDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;


import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookServiceTest {

    private BookService bookService;

    private BookDAO bookDAOMock;

    private Book book1;
    private Book book2;
    private Book book3;
    private List<Book> bookList;

    @BeforeEach
    public void setup(){

        bookDAOMock = Mockito.mock(BookDAO.class);
        bookService = new BookService(bookDAOMock);

        book1 = new Book("Left For Dead", "Steam");
        book1.setId(1);
        book2 = new Book("Metro 2033", "empresa ucraniana");
        book2.setId(2);
        book2.setWithSomeone(true);
        book3 = new Book("White Ferrari", "Frank Ocean");
        book3.setId(3);
        bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        Mockito.when(bookDAOMock.findAll()).thenReturn(bookList);
        Mockito.when(bookDAOMock.save(Mockito.any(Book.class))).thenReturn(true);
        Mockito.when(bookDAOMock.delete(Mockito.anyInt())).thenReturn(true);
        Mockito.when(bookDAOMock.update(Mockito.any(Book.class))).thenReturn(true);
    }

    @Test
    public void registerNewBookTest_shouldRegister() throws Exception {

        boolean response = bookService.registerNewBook(book3);

        assertThat(response, is(equalTo(true)));
    }

    @Test
    public void registerNewBookTest_shouldReturnThatBookIsNull(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.registerNewBook(null);
        });

        Assertions.assertEquals("Null book", thrown.getMessage());
    }

    @Test
    public void registerNewBookTest_shouldReturnThatBookAlreadyExist(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.registerNewBook(book1);
        });

        Assertions.assertEquals("Book already exists", thrown.getMessage());
    }

    @Test
    public void deleteBookTest_shouldDelete(){

        boolean response = bookService.deleteBook(1);

        assertThat(response, is(equalTo(true)));
    }

    @Test
    public void deleteBookTest_shouldInformThatListHasNoBooks(){

        bookList.clear();

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            boolean response = bookService.deleteBook(1);
        });

        assertThat(thrown.getMessage(), is(equalTo("No books in the list")));
    }

    @Test
    public void deleteBookTest_shouldInformThatBookDoesNotExist(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            boolean response = bookService.deleteBook(3);
        });

        assertThat(thrown.getMessage(), is(equalTo("This book does not exist to be deleted")));
    }

    @Test
    public void deleteBookTest_shouldInformThatBookCantBeDeletedBecauseIsWithSomeone(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            boolean response = bookService.deleteBook(2);
        });

        assertThat(thrown.getMessage(), is(equalTo("Can not delete this book because it's with someone")));
    }

    @Test
    public void updateBookTest_shouldUpdate(){

        boolean response = bookService.updateBook(book1);

        assertThat(response, is(equalTo(true)));
    }

    @Test
    public void updateBookTest_shouldInformThatInputIsNull(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.updateBook(null);
        });

        assertThat(thrown.getMessage(), is(equalTo("You can not save a client whose instance does not exist")));
    }

    @Test
    public void updateBookTest_shouldInformThatThisBookDoesNotExist(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.updateBook(book3);
        });

        assertThat(thrown.getMessage(), is(equalTo("Try to create a new book, this book does not exist")));
    }

    @Test
    public void searchBookByIdTest_shouldReturnABook(){

        bookService.searchBookById(1);

        Mockito.verify(bookDAOMock).findById(1);
    }

    @Test
    public void searchBookByAuthorTest_shouldReturnAListOfBooks(){

        bookList.add(book3);
        List<Book> bookListAuthorIsFrank = Arrays.asList(book3);

        Mockito.when(bookDAOMock.findByAuthor("Frank Ocean")).thenReturn(bookList.stream().filter(p -> p.getAuthor() == "Frank Ocean").collect(Collectors.toList()));

        var returnedBookList = bookService.searchBookByAuthor("Frank Ocean");

        assertThat(returnedBookList, is(equalTo(bookListAuthorIsFrank)));
    }

    @Test
    public void searchBookByAuthorTest_shouldInformThatNoBooksWereFound(){

        bookList.clear();

        List<Book> returnedList = bookService.searchBookByAuthor("Leão Barros");

        assertThat(returnedList, is(equalTo(Collections.EMPTY_LIST)));
    }

    @Test
    public void listAllBooksTest_shouldReturnAllBooks(){

        bookList.add(book3);

        var returnedBookList = bookService.listAllBooks();

        assertThat(returnedBookList, is(equalTo(bookList)));
    }

}
