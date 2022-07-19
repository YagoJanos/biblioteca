package com.github.yagojanos.tema8.dao;

import com.github.yagojanos.tema8.entities.Loan;
import com.github.yagojanos.tema8.entities.enums.Status;
import com.github.yagojanos.tema8.repositories.CrudRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoanDAO implements CrudRepository<Loan> {


    private static final File JSON_FILE = new File("src/main/java/com/github/yagojanos/tema8/jsondatabase/rents.json");
    private static final JSONParser JSON_PARSER = new JSONParser();

    public LoanDAO(){

    }

    @Override
    public boolean save(Loan loan) {

        List<Loan> loanList = bringFromDatabase();

        if(loanList == null){
            throw new RuntimeException("Inconsistent data, null list");
        }

        loanList.add(loan);
        System.out.println("Loan saved");
        sendToDatabase(loanList);
        return true;
    }

    public boolean update(Loan loan){

        List<Loan> loanList = bringFromDatabase();

        if(loanList == null){
            throw new RuntimeException("Inconsistent data, null list");
        }

        Loan loanToBeUpdated = loanList.stream()
                .filter(p -> p.getId() == loan.getId())
                .findFirst()
                .get();

        loanList.set(loanList.indexOf(loanToBeUpdated), loan);
        sendToDatabase(loanList);
        System.out.println("Loan was overwritten");
        return true;

    }

    public boolean delete(int id){

        List<Loan> loanList = bringFromDatabase();

        if(loanList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        if(loanList.isEmpty()){
            throw new RuntimeException("No loans in the list");
        }

        Loan loan = loanList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if(loan != null){
            loanList.remove(loan);
            sendToDatabase(loanList);
            return true;
        } else {
            throw new RuntimeException("This loan does not exist");
        }
    }

    @Override
    public Optional<Loan> findById(int id) {

        List<Loan> loanList = bringFromDatabase();

        if(loanList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        return loanList.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public List<Loan> findAll() {

        List<Loan> loanList = bringFromDatabase();

        if(loanList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        return loanList.stream().collect(Collectors.toUnmodifiableList());
    }



    public List<Loan> bringFromDatabase() {

        List<Loan> loanList = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(JSON_FILE))) {

            Object obj = JSON_PARSER.parse(bf);

            JSONArray lista = (JSONArray) obj;

            lista.forEach(loan -> loanList.add(convertJSONObjectToObject((JSONObject) loan)));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return loanList;
    }

    public Loan convertJSONObjectToObject(JSONObject jsonBookLoan){

        int id = Integer.parseInt(jsonBookLoan.get("id").toString());
        int customerId = Integer.parseInt(jsonBookLoan.get("customerId").toString());
        int bookId = Integer.parseInt(jsonBookLoan.get("bookId").toString());
        LocalDate receiveDay = LocalDate.parse(jsonBookLoan.get("receiveDay").toString());
        LocalDate theShouldReturnDay = LocalDate.parse(jsonBookLoan.get("theShouldReturnDay").toString());
        LocalDate theReturnDay;
        if(jsonBookLoan.get("theReturnDay") == null){
            theReturnDay = null;
        } else
        {
            theReturnDay = LocalDate.parse(jsonBookLoan.get("theReturnDay").toString());
        }
        Status status = Status.valueOf(jsonBookLoan.get("status").toString());
        double price = Double.parseDouble(jsonBookLoan.get("price").toString());


        Loan loan = new Loan(id, customerId, bookId);
        loan.setReceiveDay(receiveDay);
        loan.setTheShouldReturnDay(theShouldReturnDay);
        loan.setTheReturnDay(theReturnDay);
        loan.calculateDaysLate();
        loan.isLate();
        loan.setStatus(status);
        loan.setPrice(price);

        return loan;
    }

    public void sendToDatabase(List<Loan> loanList){

        JSONArray jsonArray = new JSONArray();
        loanList.forEach(p -> jsonArray.add(convertObjectToJSONObject(p)));

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(JSON_FILE))){
            bw.write(jsonArray.toJSONString());
            bw.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public JSONObject convertObjectToJSONObject(Loan loan){

        JSONObject jsonBook = new JSONObject();

        jsonBook.put("id", loan.getId());
        jsonBook.put("customerId", loan.getCustomerId());
        jsonBook.put("bookId", loan.getBookId());
        jsonBook.put("receiveDay", loan.getReceiveDay().toString());
        jsonBook.put("theShouldReturnDay", loan.getTheShouldReturnDay().toString());
        if(loan.getTheReturnDay() == null) {
            jsonBook.put("theReturnDay", null);
        } else {
            jsonBook.put("theReturnDay", loan.getTheReturnDay().toString());
        }
        jsonBook.put("status", loan.getStatus().toString());
        jsonBook.put("price", loan.getPrice());

        return jsonBook;
    }
}
