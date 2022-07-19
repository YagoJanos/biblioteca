package com.github.yagojanos.tema8.entities;

import com.github.yagojanos.tema8.entities.enums.Status;

import java.time.Duration;
import java.time.LocalDate;



public class Loan {

    private static final double valuePerDayLate = 5.00;

    private int id;

    private int customerId;
    private int bookId;
    private LocalDate receiveDay;
    private LocalDate theShouldReturnDay;
    private LocalDate theReturnDay;
    private boolean late;
    private long daysLate;
    private Status status;
    private double price = 0.0;

    public Loan(int id, int customerId, int bookId) {

        this.customerId = customerId;
        this.bookId = bookId;
        receiveDay = LocalDate.now();
        theShouldReturnDay = receiveDay.plusWeeks(1);
        status = Status.IN_PROGRESS;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReceiveDay() {
        return receiveDay;
    }

    public void setReceiveDay(LocalDate receiveDay) {
        this.receiveDay = receiveDay;
    }

    public LocalDate getTheShouldReturnDay() {
        return theShouldReturnDay;
    }

    public void setTheShouldReturnDay(LocalDate theShouldReturnDay) {
        this.theShouldReturnDay = theShouldReturnDay;
    }

    public LocalDate getTheReturnDay() {
        return theReturnDay;
    }

    public void setTheReturnDay(LocalDate theReturnDay) {
        this.theReturnDay = theReturnDay;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public boolean isLate(){

        if(LocalDate.now().isAfter(theShouldReturnDay)){

            late = true;
        } else {
            late = false;
        }
        return late;
    }

    public long calculateDaysLate(){

        if(theReturnDay != null)
            daysLate = Duration
                    .between(theShouldReturnDay.atStartOfDay(), theReturnDay.atStartOfDay())
                    .toDays();
        else
            daysLate = 0;

        return daysLate;
    }

    public boolean giveBack(){

        theReturnDay = LocalDate.now();
        status = Status.FINISHED;

        if(isLate()){

            price = calculateDaysLate() * valuePerDayLate;

            System.out.println("Value to pay: " + price);
        } else {

            System.out.println("Book received");
        }
        return true;
    }

    public boolean renew(){

        if(isLate()){
            System.out.println("Late loan, it is not possible to renew the loan");
            return false;
        } else {
            theShouldReturnDay = theShouldReturnDay.plusWeeks(1);
            return true;
        }
    }

    @Override
    public String toString() {

        return "Loan id = " + id +
                ", customerId = " + customerId +
                ", bookId = " + bookId +
                ", receiveDay = " + receiveDay +
                ", day that must return = " + theShouldReturnDay +
                ", day that actually returned = " + theReturnDay +
                ", the book is late = " + late +
                ", number of days late = " + daysLate +
                ", status = " + status +
                ", price = " + price;
    }
}














