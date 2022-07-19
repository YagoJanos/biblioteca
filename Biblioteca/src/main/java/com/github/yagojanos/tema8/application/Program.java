package com.github.yagojanos.tema8.application;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.github.yagojanos.tema8.entities.Book;
import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.services.BookService;
import com.github.yagojanos.tema8.services.CustomerService;
import com.github.yagojanos.tema8.services.LoanService;
import com.github.yagojanos.tema8.services.ReportService;
import com.github.yagojanos.tema8.dao.BookDAO;
import com.github.yagojanos.tema8.dao.CustomerDAO;
import com.github.yagojanos.tema8.dao.LoanDAO;

public class Program {

    private static int option = -1;
    private static Scanner sc = new Scanner(System.in);
    private static BookDAO bookDAO = new BookDAO();
    private static CustomerDAO customerDAO = new CustomerDAO();
    private static BookService bookService = new BookService(bookDAO);
    private static CustomerService customerService = new CustomerService(customerDAO);
    private static LoanService loanService = new LoanService(new LoanDAO(), bookService, customerService);
    private static ReportService reportService = new ReportService(bookService, customerService, loanService);

    public static void main(String[] args) {

        System.out.println("Olá, seja bem vindo ao programa de empréstimo de livros");
        try{
            while (option != 0) {

                System.out.println("Digite o que você deseja fazer");
                System.out.println("1- Customer menu");
                System.out.println("2- Book menu");
                System.out.println("3- Loan menu");
                System.out.println("4- Report menu");
                System.out.println("0- Exit book borrowing system");
                System.out.println();
                System.out.println("Type here: ");

                option = sc.nextInt();

                switch (option) {
                    case 1:
                        customerMenu();
                        break;
                    case 2:
                        bookMenu();
                        break;
                    case 3:
                        loanMenu();
                        break;
                    case 4:
                        reportMenu();
                        break;
                    case 0:
                        System.out.println("Good bye!");
                        break;
                    default:
                        System.out.println("Invalid option, enter a valid one");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void customerMenu() {

        while (option != 5) {

            System.out.println("What do you want to do?");
            System.out.println("1- Register a new customer");
            System.out.println("2- Delete a customer");
            System.out.println("3- Search customer by id");
            System.out.println("4- List all customers");
            System.out.println("5- Return to main menu");
            System.out.println();
            System.out.println("Type here: ");

            try {

                option = sc.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Tipo de dao inválido");
            }

            switch (option) {
                case 1:
                    registerNewCustomerMenu();
                    break;
                case 2:
                    deleteCustomerMenu();
                    break;
                case 3:
                    searchCustomerByIdMenu();
                    break;
                case 4:
                    reportService.printAllCustomers();
                case 5:
                    break;
                default:
                    System.out.println("Invalid option, enter a valid one");
            }
        }
    }

    private static void registerNewCustomerMenu() {

        System.out.println("Enter the customer name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.println("Enter the customer identification document: ");
        String identificationDocument = sc.next();
        Customer customer = new Customer(name, identificationDocument);
        try {
            customerService.registerNewCustomer(customer);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteCustomerMenu() {

        try {
            System.out.println("Enter the customer id: ");
            int id = sc.nextInt();

            customerService.deleteCustomer(id);
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private static void searchCustomerByIdMenu() {

        try {
            System.out.println("Enter the customer id: ");
            int id = sc.nextInt();

            System.out.println(customerService.searchCustomerById(id).get());
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (NoSuchElementException e){
            System.out.println("Customer not found");
        }
    }

    private static void bookMenu() {

        while (option != 6) {

            System.out.println("What do you want to do?");
            System.out.println("1- Register a new book");
            System.out.println("2- Delete a book");
            System.out.println("3- Search book by id");
            System.out.println("4- Search book by author");
            System.out.println("5- List all books");
            System.out.println("6- Return to main menu");
            System.out.println();
            System.out.println("Type here: ");

            try {

                option = sc.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Tipo de dao inválido");
            }

            switch (option) {
                case 1:
                    registerNewBookMenu();
                    break;
                case 2:
                    deleteBookMenu();
                    break;
                case 3:
                    searchBookByIdMenu();
                    break;
                case 4:
                    searchBookByAuthorMenu();
                    break;
                case 5:
                    reportService.printAllBooks();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid option, enter a valid one");
            }
        }
    }

    private static void registerNewBookMenu() {

        System.out.println("Enter the book title: ");
        sc.nextLine();
        String title = sc.nextLine();
        System.out.println("Enter the book author: ");
        String author = sc.nextLine();

        try {
            Book book = new Book(title, author);
            bookService.registerNewBook(book);
            System.out.println();
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteBookMenu() {

        try {
            System.out.println("Enter book id: ");
            int bookId = sc.nextInt();

            bookService.deleteBook(bookId);
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private static void searchBookByIdMenu() {

        try {
            System.out.println("Enter book id: ");
            int bookId = sc.nextInt();

            System.out.println(bookService.searchBookById(bookId).get());
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (NoSuchElementException e){
            System.out.println("Book not found");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    private static void searchBookByAuthorMenu() {

        try {
            System.out.println("Enter author name: ");
            sc.nextLine();
            String authorName = sc.nextLine();

            bookService.searchBookByAuthor(authorName).forEach(p -> System.out.println(p));
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
    }


    private static void loanMenu() {

        while (option != 4) {
            System.out.println("What do you want to do?");
            System.out.println("1- Lend a book");
            System.out.println("2- Give back");
            System.out.println("3- Renew loan");
            System.out.println("4- Return to main menu");
            System.out.println();
            System.out.println("Type here: ");

            try {

                option = sc.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Tipo de dao inválido");
            }

            switch (option) {
                case 1:
                    lendABookMenu();
                    break;
                case 2:
                    giveBackMenu();
                    break;
                case 3:
                    renewMenu();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option, enter a valid one");
            }
        }
    }

    private static void lendABookMenu() {

        try {
            System.out.println("Enter the customer ID: ");
            int customerId = sc.nextInt();
            System.out.println("Enter the book ID: ");
            int bookId = sc.nextInt();
            loanService.createLoan(customerId, bookId);
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    private static void giveBackMenu() {

        try {
            System.out.println("Enter the customer ID: ");
            int customerId = sc.nextInt();
            System.out.println("Enter the book ID: ");
            int bookId = sc.nextInt();

            loanService.finishLoan(customerId, bookId);
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    private static void renewMenu() {

        try {
            System.out.println("Enter the customer ID: ");
            int customerId = sc.nextInt();
            System.out.println("Enter the book ID: ");
            int bookId = sc.nextInt();

            loanService.renewLoan(customerId, bookId);
        } catch (InputMismatchException e){
            System.out.println("Invalid answer");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    private static void reportMenu() {
        while (option != 5) {
            System.out.println("What do you want to see?");
            System.out.println("1- All books lent");
            System.out.println("2- All books lent and their customers");
            System.out.println("3- Top 10 customers");
            System.out.println("4- Late loans");
            System.out.println("5- Return to main menu");
            System.out.println();
            System.out.println("Type here: ");


            try {

                option = sc.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Tipo de dao inválido");
            }

            switch (option) {
                case 1:
                    reportService.printAllBooksLent();
                    break;
                case 2:
                    reportService.printBooksThatWereBorrowedAndTheirCustomers();
                    break;
                case 3:
                    reportService.printTop10Customers();
                    break;
                case 4:
                    reportService.printLateLoans();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option, enter a valid one");
            }
        }
    }
}
