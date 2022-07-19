package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Book;
import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.entities.enums.Status;
import com.github.yagojanos.tema8.entities.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReportServiceTest {

    private ReportService reportService;

    private BookService bookServiceMock;
    private LoanService loanServiceMock;
    private CustomerService customerServiceMock;
    private List<Customer> customerList;
    private List<Book> bookList;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customer4;
    private Customer customer5;
    private Customer customer6;
    private Customer customer7;
    private Customer customer8;
    private Customer customer9;
    private Customer customer10;
    private Customer customer11;
    private Customer customer12;
    private Customer customer13;

    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;
    private Book book5;
    private Book book6;
    private Book book7;
    private Book book8;
    private Book book9;
    private Book book10;
    private Book book11;
    private Book book12;
    private Book book13;

    private Loan loan1;
    private Loan loan2;
    private Loan loan3;
    private Loan loan4;
    private Loan loan5;
    private Loan loan6;
    private Loan loan7;
    private Loan loan8;
    private Loan loan9;
    private Loan loan10;
    private Loan loan11;
    private Loan loan12;
    private Loan loan13;
    private Loan loan14;

    private List<Loan> loanList;

    @BeforeEach
    public void setup(){

        bookServiceMock = Mockito.mock(BookService.class);
        customerServiceMock = Mockito.mock(CustomerService.class);
        loanServiceMock = Mockito.mock(LoanService.class);

        reportService = new ReportService(bookServiceMock, customerServiceMock, loanServiceMock);

        customer1 = new Customer("Yago", "109");
        customer1.setId(1);
        customer1.setBooksWithCustomerEverQty(1);
        customer2 = new Customer("Bia", "342");
        customer2.setId(2);
        customer2.setBooksWithCustomerEverQty(2);
        customer3 = new Customer("Lucas", "7654");
        customer3.setId(3);
        customer3.setBooksWithCustomerEverQty(3);
        customer4 = new Customer("Maria", "345");
        customer4.setId(4);
        customer4.setBooksWithCustomerEverQty(4);
        customer5 = new Customer("João", "625");
        customer5.setId(5);
        customer5.setBooksWithCustomerEverQty(5);
        customer6 = new Customer("Kleber", "994");
        customer6.setId(6);
        customer6.setBooksWithCustomerEverQty(6);
        customer7 = new Customer("Jonatan", "10234");
        customer7.setId(7);
        customer7.setBooksWithCustomerEverQty(7);
        customer8 = new Customer("Libero", "8341");
        customer8.setId(8);
        customer8.setBooksWithCustomerEverQty(8);
        customer9 = new Customer("Natan", "23");
        customer9.setId(9);
        customer9.setBooksWithCustomerEverQty(9);
        customer10 = new Customer("Leandro", "43");
        customer10.setId(10);
        customer10.setBooksWithCustomerEverQty(10);
        customer11 = new Customer("Letícia", "524");
        customer11.setId(11);
        customer11.setBooksWithCustomerEverQty(11);
        customer12 = new Customer("Parati", "239");
        customer12.setId(12);
        customer12.setBooksWithCustomerEverQty(12);
        customer13 = new Customer("Marcel", "12313");
        customer13.setId(13);
        customer13.setBooksWithCustomerEverQty(13);

        customerList = List.of(customer1,customer2,customer3,customer4,customer5,customer6,customer7,customer8,customer9,customer10,customer11,customer12,customer13);

        book1 = new Book("StarWars1", "Lucas Arts");
        book1.setCustomerWithThisBookId(1);
        book1.setId(1);
        book1.setWithSomeone(true);
        book2 = new Book("StarWars2", "Lucas Arts");
        book2.setCustomerWithThisBookId(1);
        book2.setId(2);
        book2.setWithSomeone(true);
        book3 = new Book("StarWars3", "Lucas Arts");
        book3.setCustomerWithThisBookId(1);
        book3.setId(3);
        book3.setWithSomeone(true);
        book4 = new Book("StarWars4", "Lucas Arts");
        book4.setCustomerWithThisBookId(2);
        book4.setId(4);
        book4.setWithSomeone(true);
        book5 = new Book("StarWars5", "Lucas Arts");
        book5.setCustomerWithThisBookId(2);
        book5.setId(5);
        book5.setWithSomeone(true);
        book6 = new Book("StarWars6", "Lucas Arts");
        book6.setCustomerWithThisBookId(2);
        book6.setId(6);
        book6.setWithSomeone(true);
        book7 = new Book("StarWars7", "Disney");
        book7.setCustomerWithThisBookId(3);
        book7.setId(7);
        book7.setWithSomeone(true);
        book8 = new Book("StarWars8", "Disney");
        book8.setCustomerWithThisBookId(4);
        book8.setId(8);
        book8.setWithSomeone(true);
        book9 = new Book("StarWars9", "Disney");
        book9.setCustomerWithThisBookId(5);
        book9.setId(9);
        book9.setWithSomeone(true);
        book10 = new Book("StarWars10", "Disney");
        book10.setCustomerWithThisBookId(6);
        book10.setId(10);
        book10.setWithSomeone(true);
        book11 = new Book("StarWars11", "Disney");
        book11.setCustomerWithThisBookId(7);
        book11.setId(11);
        book11.setWithSomeone(true);
        book12 = new Book("StarWars12", "Disney");
        book12.setCustomerWithThisBookId(8);
        book12.setId(12);
        book12.setWithSomeone(true);
        book13 = new Book("StarWars13", "Disney");
        book13.setCustomerWithThisBookId(0);
        book13.setId(13);
        book13.setWithSomeone(false);

        bookList = List.of(book1,book2,book3,book4,book5,book6,book7,book8,book9,book10,book11,book12,book13);

        loan1 = new Loan(1,1,1);
        loan1.setStatus(Status.IN_PROGRESS);
        loan1.setLate(true);
        loan1.setTheShouldReturnDay(LocalDate.now().minusDays(1));
        loan2 = new Loan(2,2,1);
        loan2.setStatus(Status.IN_PROGRESS);
        loan2.setLate(true);
        loan2.setTheShouldReturnDay(LocalDate.now().minusDays(1));
        loan3 = new Loan(3,3,1);
        loan3.setStatus(Status.IN_PROGRESS);
        loan3.setLate(true);
        loan3.setTheShouldReturnDay(LocalDate.now().minusDays(1));
        loan4 = new Loan(4,4,2);
        loan4.setStatus(Status.IN_PROGRESS);
        loan4.setLate(true);
        loan4.setTheShouldReturnDay(LocalDate.now().minusDays(1));
        loan5 = new Loan(5,5,2);
        loan5.setStatus(Status.IN_PROGRESS);
        loan6 = new Loan(6,6,2);
        loan6.setStatus(Status.IN_PROGRESS);
        loan7 = new Loan(7,7,3);
        loan7.setStatus(Status.IN_PROGRESS);
        loan8 = new Loan(8,8,4);
        loan8.setStatus(Status.IN_PROGRESS);
        loan9 = new Loan(9,9,5);
        loan9.setStatus(Status.IN_PROGRESS);
        loan10 = new Loan(10,10,6);
        loan10.setStatus(Status.IN_PROGRESS);
        loan11 = new Loan(11,11,7);
        loan11.setStatus(Status.IN_PROGRESS);
        loan12 = new Loan(12,12,8);
        loan12.setStatus(Status.IN_PROGRESS);
        loan13 = new Loan(13,5,7);
        loan13.setStatus(Status.FINISHED);
        loan14 = new Loan(14,2,4);
        loan14.setStatus(Status.FINISHED);

        loanList = Arrays.asList(loan1, loan2, loan3, loan4, loan5, loan6, loan7, loan8, loan9, loan10, loan11, loan12, loan13, loan14);
    }

    @Test
    public void allBooksLentTest(){

        Mockito.when(bookServiceMock.listAllBooks()).thenReturn(bookList);
        var result = reportService.allBooksLent();

        Mockito.verify(bookServiceMock).listAllBooks();

        assertThat(result.size(), is(bookList.size() - 1));
    }

    @Test
    public void top10CustomersTest(){

        Mockito.when(customerServiceMock.listAllCustomers()).thenReturn(customerList);
        var top10List = reportService.top10Customers();

        Mockito.verify(customerServiceMock).listAllCustomers();
        assertThat(top10List.size(), is(10));
    }

    @Test
    public void currentLoansTest(){

        Mockito.when(loanServiceMock.showLoanHistory()).thenReturn(loanList);

        var returnedValue = reportService.currentLoans();

        assertThat(returnedValue.size(), is(equalTo(12)));

    }

    @Test
    public void lateLoansTest(){

        Mockito.when(loanServiceMock.showLoanHistory()).thenReturn(loanList);

        var returnedValue = reportService.lateLoans();

        assertThat(returnedValue.size(), is(equalTo(4)));

    }

}
