package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.dao.CustomerDAO;

import java.util.List;
import java.util.Optional;

public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO){
        this.customerDAO = customerDAO;
    }

    public boolean registerNewCustomer(Customer customer){

        if(customerDAO.findAll().stream().anyMatch(p -> p.getId() == customer.getId())) {
            throw new RuntimeException("Customer already exists");
        }

        customerDAO.save(customer);
        return true;
    }

    public boolean deleteCustomer(int id) {

        List<Customer> allCustomersList = customerDAO.findAll();

        if(allCustomersList.size() == 0){
            throw new RuntimeException("No customers in the list");
        }

        Customer customer = allCustomersList
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if(customer == null){
            throw new RuntimeException("This customer does not exist to be deleted");
        }

        if(customer.getBooksWithCustomerNowQty() != 0){
            throw new RuntimeException("This costumer has books with him, can not be deleted");
        }

        customerDAO.delete(id);
        return true;
    }

    public boolean updateCustomer(Customer customer){

        if(customer == null){
            throw new RuntimeException("You can not save a customer whose instance does not exist");
        }

        if(customerDAO.findAll().stream().anyMatch(p -> p.getId() == customer.getId())) {
            customerDAO.update(customer);
            return true;
        } else {
            throw new RuntimeException("Try to create a new customer, this customer does not exist");
        }
    }

    public Optional<Customer> searchCustomerById(int id){

        return customerDAO.findById(id);
    }

    public List<Customer> listAllCustomers(){
        return customerDAO.findAll();
    }

}
