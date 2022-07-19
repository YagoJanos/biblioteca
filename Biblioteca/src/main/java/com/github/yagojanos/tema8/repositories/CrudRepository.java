package com.github.yagojanos.tema8.repositories;


import java.util.List;
import java.util.Optional;

public interface CrudRepository <T>{

    boolean save(T obj);
    boolean update(T obj);
    boolean delete(int id);
    Optional<T> findById(int id);
    List<T> findAll();

    List<T> bringFromDatabase();

    void sendToDatabase(List<T> list);
    
}
