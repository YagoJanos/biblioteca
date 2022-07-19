package com.github.yagojanos.tema8.dao;

import com.github.yagojanos.tema8.entities.Book;
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

public class BookDAO implements CrudRepository<Book> {

    private static final File jsonFile = new File("src/main/java/com/github/yagojanos/tema8/jsondatabase/books.json");
    private static final JSONParser jsonParser = new JSONParser();

    public BookDAO(){

    }

    @Override
    public boolean save(Book book) {

        List<Book> bookList = bringFromDatabase();

        if(bookList == null){
            throw new RuntimeException("Inconsistent data, null list");
        }

        bookList.add(book);
        System.out.println("Book saved");
        sendToDatabase(bookList);
        return true;

    }

    public boolean update(Book book){

        List<Book> bookList = bringFromDatabase();

        if(bookList == null){
            throw new RuntimeException("Inconsistent data, null list");
        }

        Book bookToBeUpdated = bookList.stream()
                .filter(p -> p.getId() == book.getId())
                .findFirst()
                .get();
        bookList.set(bookList.indexOf(bookToBeUpdated), book);
        sendToDatabase(bookList);
        System.out.println("Book was overwritten");
        return true;
    }

    @Override
    public boolean delete(int id){

        List<Book> bookList = bringFromDatabase();

        if(bookList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        Book book = bookList.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        bookList.remove(book);
        sendToDatabase(bookList);
        return true;
    }


    @Override
    public Optional<Book> findById(int id) {

        List<Book> bookList = bringFromDatabase();

        if(bookList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        return bookList.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Book> findByAuthor(String autor){

        List<Book> bookList = bringFromDatabase();

        if(bookList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        return bookList.stream().filter(p -> p.getAuthor().equals(autor)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Book> findAll() {
        List<Book> bookList = bringFromDatabase();

        if(bookList == null ){
            throw new RuntimeException("Inconsistent data, null list");
        }

        return bookList.stream().collect(Collectors.toUnmodifiableList());
    }

    public List<Book> bringFromDatabase() {

        List<Book> bookList = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(jsonFile))) {

            Object obj = jsonParser.parse(bf);

            JSONArray lista = (JSONArray) obj;

            lista.forEach(book -> bookList.add(convertJSONObjectToObject((JSONObject) book)));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public Book convertJSONObjectToObject(JSONObject jsonBook){

        int id = Integer.parseInt(jsonBook.get("id").toString());
        String title = jsonBook.get("title").toString();
        String author = jsonBook.get("author").toString();
        boolean withSomeone = Boolean.parseBoolean(jsonBook.get("withSomeone").toString());
        int custumerWithThisBookId = Integer.parseInt(jsonBook.get("customerWithThisBookId").toString());

        Book book = new Book(id, title, author);
        book.setWithSomeone(withSomeone);
        book.setCustomerWithThisBookId(custumerWithThisBookId);
        return book;
    }

    public void sendToDatabase(List<Book> bookList){

        JSONArray jsonArray = new JSONArray();
        bookList.forEach(p -> jsonArray.add(convertObjectToJSONObject(p)));

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(jsonFile))){
            bw.write(jsonArray.toJSONString());
            bw.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public JSONObject convertObjectToJSONObject(Book book){

        JSONObject jsonBook = new JSONObject();

        jsonBook.put("id", Integer.toString(book.getId()));
        jsonBook.put("title", book.getTitle());
        jsonBook.put("author", book.getAuthor());
        jsonBook.put("withSomeone", Boolean.toString(book.isWithSomeone()));
        jsonBook.put("customerWithThisBookId", Integer.toString(book.getCustomerWithThisBookId()));

        return jsonBook;
    }

}

