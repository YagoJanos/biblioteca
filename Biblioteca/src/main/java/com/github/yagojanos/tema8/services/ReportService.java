package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Book;
import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.entities.enums.Status;
import com.github.yagojanos.tema8.entities.Loan;

import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

    private BookService bookService;
    private LoanService loanService;
    private CustomerService customerService;

    public ReportService(BookService bookService, CustomerService customerService, LoanService loanService) {

        this.bookService = bookService;
        this.loanService = loanService;
        this.customerService = customerService;
    }

    public void printAllBooks(){
        bookService.listAllBooks().forEach(p -> System.out.println(p));
    }

    public void printAllCustomers(){
        customerService.listAllCustomers().forEach(p -> System.out.println(p));
    }

    public List<Book> allBooksLent(){
        return bookService.listAllBooks().stream().filter(p -> p.isWithSomeone()).collect(Collectors.toUnmodifiableList());
    }

    public void printAllBooksLent(){
        allBooksLent().forEach(p -> System.out.println(p));
    }


    public List<Customer> top10Customers(){
        return customerService.listAllCustomers().stream().sorted(Comparator.comparing(Customer::getBooksWithCustomerEverQty).reversed()).limit(10).collect(Collectors.toUnmodifiableList());
    }

    public void printTop10Customers(){
        top10Customers().forEach(p -> System.out.println(p));
    }

    public List<Loan> currentLoans(){
        return loanService.showLoanHistory().stream().filter(p -> p.getStatus() == Status.IN_PROGRESS).collect(Collectors.toUnmodifiableList());
    }

    public void printBooksThatWereBorrowedAndTheirCustomers(){

        List<Loan> currentLoanList = currentLoans();

        Customer customer;
        Book book;

        for(Loan loan : currentLoanList){
            customer = customerService.listAllCustomers().stream().filter(p -> p.getId() == loan.getCustomerId()).findFirst().get();
            book = bookService.listAllBooks().stream().filter(p -> p.getId() == loan.getBookId()).findFirst().get();
            System.out.println("Book id: " + book.getId() + " , book name: " + book.getAuthor() + " , customer id: " + customer.getId() + " , client name: " + customer.getName());
        }
    }

    public List<Loan> lateLoans(){
        return loanService.showLoanHistory().stream().filter(p -> p.isLate() == true).collect(Collectors.toUnmodifiableList());
    }

    public void printLateLoans(){

        List<Loan> lateLoanList = lateLoans();

        Customer customer;
        Book book;

        for(Loan loan : lateLoanList){
            customer = customerService.listAllCustomers().stream().filter(p -> p.getId() == loan.getCustomerId()).findFirst().get();
            book = bookService.listAllBooks().stream().filter(p -> p.getId() == loan.getBookId()).findFirst().get();
            System.out.println("Book id: " + book.getId() + " , book name: " + book.getAuthor() + " , customer id: " + customer.getId() + " , customer name: " + customer.getName());
        }
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
