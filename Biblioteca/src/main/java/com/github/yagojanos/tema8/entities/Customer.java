package com.github.yagojanos.tema8.entities;

import com.github.yagojanos.tema8.services.CustomerService;
import com.github.yagojanos.tema8.dao.CustomerDAO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Customer {

    private CustomerService customerService = new CustomerService(new CustomerDAO());

    private static int idCounter;

    private int id;

    private String name;

    private String document;
    private List<Integer> iDOfBooksWithCustomerNow = new ArrayList<>();

    private int booksWithCustomerNowQty = 0;
    private int booksWithCustomerEverQty = 0;

    public Customer(String name, String document){
        updateId();
        this.name = name;
        this.document = document;
    }

    public Customer(int id, String name, String document) {
        this.id = id;
        this.name = name;
        this.document = document;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public List<Integer> getiDOfBooksWithCustomerNow() {
        return iDOfBooksWithCustomerNow;
    }

    public void setiDOfBooksWithCustomerNow(List<Integer> iDOfBooksWithCustomerNow) {
        this.iDOfBooksWithCustomerNow = iDOfBooksWithCustomerNow;
    }

    public int getBooksWithCustomerNowQty() {
        return booksWithCustomerNowQty;
    }

    public void setBooksWithCustomerNowQty(int booksWithCustomerNowQty) {
        this.booksWithCustomerNowQty = booksWithCustomerNowQty;
    }

    public int getBooksWithCustomerEverQty() {
        return booksWithCustomerEverQty;
    }

    public void setBooksWithCustomerEverQty(int booksWithCustomerEverQty) {
        this.booksWithCustomerEverQty = booksWithCustomerEverQty;
    }



    public boolean borrowBook(int id){

        if(booksWithCustomerNowQty <= 5){
            iDOfBooksWithCustomerNow.add(id);
            booksWithCustomerNowQty++;
            booksWithCustomerEverQty++;
            return true;
        } else {
            System.out.println("Customer can not borrow more books");
            return false;
        }
    }

    public boolean giveBackBook(int id){

        if(iDOfBooksWithCustomerNow.stream().anyMatch(p -> p == id)){

            iDOfBooksWithCustomerNow.removeIf(p -> p == id);
            booksWithCustomerNowQty--;
            return true;
        } else {
            System.out.println("This costumer is not with this book");
            return false;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    private void updateId() {

        if(customerService.listAllCustomers().size() > 0){

            idCounter = customerService.listAllCustomers().stream()
                    .max(Comparator.comparing(Customer::getId))
                    .get()
                    .getId();
        } else {
            idCounter = 0;
        }
        this.id = ++idCounter;
    }

    @Override
    public String toString() {
        return "Customer id = " + id +
                ", name = " + name +
                ", document =" + document +
                ", IDs Of books with this customernNow = " + iDOfBooksWithCustomerNow +
                ", number of books with this costumer now = " + booksWithCustomerNowQty +
                ", number of books that this costumer ever borrowed = " + booksWithCustomerEverQty;
    }
}

