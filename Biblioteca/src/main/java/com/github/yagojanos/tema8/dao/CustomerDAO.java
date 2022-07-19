package com.github.yagojanos.tema8.dao;

import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.repositories.CrudRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerDAO implements CrudRepository<Customer> {

    private static final File jsonFile = new File("src/main/java/com/github/yagojanos/tema8/jsondatabase/clients.json");
    private static final JSONParser jsonParser = new JSONParser();

    public CustomerDAO(){

    }

    @Override
    public boolean save(Customer customer) {
        List<Customer> customerList = bringFromDatabase();

        if(customerList == null){
            throw new RuntimeException("Inconsistent data, null list");
        }

        customerList.add(customer);
        System.out.println("Customer saved");
        sendToDatabase(customerList);
        return true;
    }


    public boolean update(Customer customer){
        List<Customer> customerList = bringFromDatabase();

        if(customerList == null){
            throw new RuntimeException("Inconsistent data, null list");
        }

        Customer customerToBeUpdated = customerList.stream()
                .filter(p -> p.getId() == customer.getId())
                .findFirst()
                .get();
        customerList.set(customerList.indexOf(customerToBeUpdated), customer);
        sendToDatabase(customerList);
        System.out.println("Customer was overwritten");
        return true;

    }



    @Override
    public boolean delete(int id){
        List<Customer> customerList = bringFromDatabase();

        if(customerList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        Customer customer = customerList
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        customerList.remove(customer);
        sendToDatabase(customerList);
        return true;
    }

    @Override
    public Optional<Customer> findById(int id) {
        List<Customer> customerList = bringFromDatabase();

        if(customerList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        return customerList.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customerList = bringFromDatabase();

        if(customerList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        return customerList.stream().collect(Collectors.toUnmodifiableList());
    }


    @Override
    public List<Customer> bringFromDatabase() {

        List<Customer> customerList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(jsonFile))){

            Object obj = jsonParser.parse(br);
            JSONArray jsonArray = (JSONArray) obj;
            if(jsonArray != null){

                jsonArray.forEach(p -> customerList.add(convertJSONObjectToObject((JSONObject) p)));
            }

        }catch (IOException | ParseException e){
            e.printStackTrace();
        }

        return customerList;
    }

    public Customer convertJSONObjectToObject(JSONObject jsonObject) {

        int id = Integer.parseInt(jsonObject.get("id").toString());
        String name = jsonObject.get("name").toString();
        String document = jsonObject.get("document").toString();

        List<Integer> iDOfBooksWithCustomerNow = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) jsonObject.get("iDOfBooksWithCustomerNow");

        int bookId;
        for(Object obj : jsonArray){
            bookId = Integer.parseInt(obj.toString());
            iDOfBooksWithCustomerNow.add(bookId);
        }

        int booksWithCustomerNowQty = Integer.parseInt(jsonObject.get("booksWithCustomerNowQty").toString());
        int booksWithCustomerEverQty = Integer.parseInt(jsonObject.get("booksWithCustomerEverQty").toString());

        Customer customer = new Customer(id, name, document);
        customer.setiDOfBooksWithCustomerNow(iDOfBooksWithCustomerNow);
        customer.setBooksWithCustomerNowQty(booksWithCustomerNowQty);
        customer.setBooksWithCustomerEverQty(booksWithCustomerEverQty);

        return customer;
    }

    @Override
    public void sendToDatabase(List<Customer> list) {
        JSONArray jsonArray = new JSONArray();
        list.forEach(p -> jsonArray.add(convertObjectToJSONObject(p)));

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(jsonFile))) {
            bw.write(jsonArray.toJSONString());
            bw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public JSONObject convertObjectToJSONObject(Customer customer) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", customer.getId());
        jsonObject.put("name", customer.getName());
        jsonObject.put("document", customer.getDocument());

        JSONArray iDOfBooksWithCustomerNow = new JSONArray();

        customer.getiDOfBooksWithCustomerNow()
                .forEach(p -> iDOfBooksWithCustomerNow.add(p));

        jsonObject.put("iDOfBooksWithCustomerNow", iDOfBooksWithCustomerNow);
        jsonObject.put("booksWithCustomerNowQty", customer.getBooksWithCustomerNowQty());
        jsonObject.put("booksWithCustomerEverQty", customer.getBooksWithCustomerEverQty());

        return jsonObject;
    }


}
