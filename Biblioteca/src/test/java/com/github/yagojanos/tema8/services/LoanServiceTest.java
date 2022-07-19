package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Book;
import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.entities.Loan;
import com.github.yagojanos.tema8.dao.LoanDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoanServiceTest {

    private LoanService loanService;

    private LoanDAO loanDAOMock;
    private BookService bookServiceMock;
    private CustomerService customerServiceMock;

    private Customer customer1;
    private Customer customer2;
    private Book book1;
    private Book book2;
    private Loan loan1;
    private List<Integer> listOfBooksWithCustomer2;
    private List<Customer> customerList;
    private List<Book> bookList;
    private List<Loan> loanList;

    @BeforeEach
    public void setup() {

        loanDAOMock = Mockito.mock(LoanDAO.class);
        bookServiceMock = Mockito.mock(BookService.class);
        customerServiceMock = Mockito.mock(CustomerService.class);

        loanService = new LoanService(loanDAOMock, bookServiceMock, customerServiceMock);

        customer1 = new Customer("Yago", "1231");
        customer2 = new Customer("Bia", "123");
        customer2.setId(2);

        book1 = new Book("Carrossel", "Cirilo");
        book1.setWithSomeone(false);
        book2 = new Book("O grito", "Japonês");
        book2.setId(2);

        loan1 = new Loan(1, 2, 2);
        loan1.setId(1);
        loan1.setBookId(2);
        loan1.setCustomerId(2);

        book2.setWithSomeone(true);
        listOfBooksWithCustomer2 = new ArrayList<>();
        listOfBooksWithCustomer2.add(2);
        customer2.setiDOfBooksWithCustomerNow(listOfBooksWithCustomer2);

        customerList = List.of(customer1, customer2);
        bookList = List.of(book1, book2);
        loanList = new ArrayList<>();
        loanList.add(loan1);

        Mockito.when(customerServiceMock.searchCustomerById(1)).thenReturn(Optional.of(customer1));
        Mockito.when(customerServiceMock.searchCustomerById(2)).thenReturn(Optional.of(customer2));
        Mockito.when(bookServiceMock.searchBookById(1)).thenReturn(Optional.of(book1));
        Mockito.when(bookServiceMock.searchBookById(2)).thenReturn(Optional.of(book2));
        Mockito.when(loanDAOMock.findAll()).thenReturn(loanList);
        Mockito.when(loanService.showLoanHistory()).thenReturn(loanList);
    }

    @Test
    public void createLoan_shouldCreate() {

        boolean returnedValue = loanService.createLoan(customer1.getId(), book1.getId());

        assertThat(returnedValue, is(true));
    }

    @Test
    public void createLoan_shouldInformClientNotFound() {

        Customer customer3 = new Customer("Teste", "123");
        customer3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.createLoan(customer3.getId(), book1.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Client not found")));


    }

    @Test
    public void createLoan_shouldInformBookNotFound() {

        Book book3 = new Book("Teste", "123");
        book3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.createLoan(customer1.getId(), book3.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Book not found")));

    }

    @Test
    public void createLoan_shouldInformBookIsWithSomeone() {

        book1.setWithSomeone(true);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.createLoan(customer1.getId(), book1.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Unsuccessful operation, this book was loaned to someone")));
    }

    @Test
    public void createLoan_shouldInformCostumerCanNotBorrowOtherBook() {

        customer2.setBooksWithCustomerNowQty(5);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.createLoan(customer2.getId(), book1.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Unsuccessful operation, the costumer can not borrow any other book")));
    }


    @Test
    public void renewLoan_shouldRenew() {

        boolean returnedValue = loanService.renewLoan(customer2.getId(), book2.getId());

        assertThat(returnedValue, is(true));
    }

    @Test
    public void renewLoan_shouldInformClientNotFound() {

        Customer customer3 = new Customer("Ljl", "3930");
        customer3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.renewLoan(customer3.getId(), book2.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Client not found")));
    }

    @Test
    public void renewLoan_shouldInformBookNotFound() {

        Book book3 = new Book("Marauto", "Ribeiro");
        book3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.renewLoan(customer1.getId(), book3.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Book not found")));
    }

    @Test
    public void renewLoan_shouldInformCLientIsNotWithTheBook() {

        Book book3 = new Book("Marauto", "Ribeiro");
        book3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.renewLoan(customer2.getId(), book1.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Unsuccessful operation, this client has not this book")));
    }

    @Test
    public void renewLoan_shouldInformBookIsLate() {

        loan1.setTheShouldReturnDay(LocalDate.now().minusDays(2));

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.renewLoan(customer2.getId(), book2.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Unsuccessful operation, this book is late")));
    }

    @Test
    public void finishLoan_shouldFinish() {

        boolean returnedValue = loanService.finishLoan(customer2.getId(), book2.getId());

        assertThat(returnedValue, is(true));
    }

    @Test
    public void finishLoan_shouldInformClientNotFound() {

        Customer customer3 = new Customer("Cliente fora da lista", "09303");
        customer3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            boolean returnedValue = loanService.finishLoan(customer3.getId(), book2.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Client not found")));
    }

    @Test
    public void finishLoan_shouldInformBookNotFound() {

        Book book3 = new Book("One Piece", "466473");
        book3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            boolean returnedValue = loanService.finishLoan(customer2.getId(), book3.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Book not found")));
    }

    @Test
    public void finishLoan_shouldInformClientDidNotBorrowTheBook() {

        Customer customer3 = new Customer("Cliente fora da lista", "09303");
        customer3.setId(3);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            boolean returnedValue = loanService.finishLoan(customer3.getId(), book2.getId());

        });

        assertThat(thrown.getMessage(), is(equalTo("Client not found")));
    }

    @Test
    public void showLoanHistory_shouldShowLoanHistory() {

        loanService.showLoanHistory();

        Mockito.verify(loanDAOMock).findAll();
    }

    @Test
    public void updateLoan_shouldUpdate() {

        boolean returnedValue = loanService.updateLoan(loan1);

        assertThat(returnedValue, is(true));
    }

    @Test
    public void updateLoan_shouldInformThatInputIsNull() {

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            boolean returnedValue = loanService.updateLoan(null);

        });

        assertThat(thrown.getMessage(), is(equalTo("You can not update a loan whose instance does not exist")));
    }

    @Test
    public void updateLoan_shouldInformThatThisLoanDoesNotExist() {

        loanList.remove(0);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {

            loanService.updateLoan(loan1);

        });

        assertThat(thrown.getMessage(), is(equalTo("Try to create another loan, this loan does not exist")));
    }


}
