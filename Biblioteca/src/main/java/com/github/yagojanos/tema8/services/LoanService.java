package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.entities.Loan;
import com.github.yagojanos.tema8.dao.LoanDAO;

import com.github.yagojanos.tema8.entities.Book;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LoanService {

    private LoanDAO loanDAO;
    private BookService bookService;
    private CustomerService customerService;

    private static int idCounter;

    public LoanService(LoanDAO loanDAO, BookService bookService, CustomerService customerService){

        this.loanDAO = loanDAO;
        this.bookService = bookService;
        this.customerService = customerService;
    }

    private int updateId() {

        if(showLoanHistory().size() > 0){

            idCounter = showLoanHistory().stream()
                    .max(Comparator.comparing(Loan::getId))
                    .get()
                    .getId();
        } else {
            idCounter = 0;
        }
        return ++idCounter;
    }

    public boolean createLoan(int idCustomer, int idBook) {

        Optional<Customer> optionalCustomer = customerService.searchCustomerById(idCustomer);
        Optional<Book> optionalBook = bookService.searchBookById(idBook);

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        if (optionalBook.isEmpty()) {
            throw new RuntimeException("Book not found");
        }

        Customer customer = optionalCustomer.get();
        Book book = optionalBook.get();
        Loan loan;

        if (book.isWithSomeone()) {
            throw new RuntimeException("Unsuccessful operation, this book was loaned to someone");
        } else if (customer.getBooksWithCustomerNowQty() >= 5) {
            throw new RuntimeException("Unsuccessful operation, the costumer can not borrow any other book");
        } else {

            customer.borrowBook(idBook);

            book.setCustomerWithThisBookId(idCustomer);
            book.setWithSomeone(true);

            loan = new Loan(updateId(),idCustomer, idBook);

            if(loanDAO.findAll().stream().anyMatch(p -> p.getId() == loan.getId())){
                throw new RuntimeException("Loan already exists");
            }

            loanDAO.save(loan);
            customerService.updateCustomer(customer);
            bookService.updateBook(book);
            System.out.println("Successful operation");
            return true;
        }
    }

    public boolean renewLoan(int idCustomer, int idBook) {

        Optional<Customer> optionalCustomer = customerService.searchCustomerById(idCustomer);
        Optional<Book> optionalBook = bookService.searchBookById(idBook);

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        if (optionalBook.isEmpty()) {
            throw new RuntimeException("Book not found");
        }

        Customer customer = optionalCustomer.get();

        Loan loan;

        if ( !customer.getiDOfBooksWithCustomerNow().contains(idBook)) {
            throw new RuntimeException("Unsuccessful operation, this client has not this book");
        }

        loan = loanDAO.findAll().stream().filter(p -> p.getBookId() == idBook && p.getCustomerId() == idCustomer).findFirst().get();

        if(loan == null){
            throw new RuntimeException("Something goes wrong, can't find this loan");
        }

        if (loan.isLate()) {
            throw new RuntimeException("Unsuccessful operation, this book is late");
        }

        loan.renew();
        updateLoan(loan);

        System.out.println("Successful renew operation");
        System.out.println("New date to return book: " + loan.getTheShouldReturnDay());
        return true;
    }

    public boolean finishLoan(int idCustomer, int idBook) {

        Optional<Customer> optionalCustomer = customerService.searchCustomerById(idCustomer);
        Optional<Book> optionalBook = bookService.searchBookById(idBook);

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        if (optionalBook.isEmpty()) {
            throw new RuntimeException("Book not found");
        }

        Customer customer = optionalCustomer.get();
        Book book = optionalBook.get();
        Loan loan;

        if ( !customer.getiDOfBooksWithCustomerNow().contains(idBook)) {

            throw new RuntimeException("Client did not borrow this book");
        }

        Optional<Loan> optionalLoan = loanDAO.findAll().stream().filter(p -> p.getCustomerId() == idCustomer && p.getBookId() == idBook).findFirst();

        if (optionalLoan.isPresent()) {
            loan = optionalLoan.get();
        } else {
            throw new RuntimeException("Inconsistency, for some reason the rent is not stored");
        }

        loan.giveBack();
        updateLoan(loan);

        customer.giveBackBook(book.getId());
        customerService.updateCustomer(customer);

        book.setCustomerWithThisBookId(0);
        book.setWithSomeone(false);
        bookService.updateBook(book);
        return true;
    }

    public List<Loan> showLoanHistory() {
        return loanDAO.findAll();
    }

    public boolean updateLoan(Loan loan) {

        if (loan == null) {
            throw new RuntimeException("You can not update a loan whose instance does not exist");
        }

        if (loanDAO.findAll().stream().anyMatch(p -> p.getId() == loan.getId())) {
            loanDAO.update(loan);
            return true;
        } else {
            throw new RuntimeException("Try to create another loan, this loan does not exist");
        }
    }

}
