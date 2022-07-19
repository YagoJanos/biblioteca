package com.github.yagojanos.tema8.services;

import com.github.yagojanos.tema8.entities.Customer;
import com.github.yagojanos.tema8.dao.CustomerDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class CustomerServiceTest {

    private CustomerService customerService;

    private CustomerDAO customerDAOMock;


    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private List<Customer> customerList;

    @BeforeEach
    public void setup(){

        customerDAOMock = Mockito.mock(CustomerDAO.class);
        customerService = new CustomerService(customerDAOMock);

        customer1 = new Customer("Homem aranha", "3420");
        customer1.setId(1);
        customer2 = new Customer("Thor", "4920");
        customer2.setId(2);
        customer2.setBooksWithCustomerNowQty(2);
        customer3 = new Customer("Eu", "3049");
        customer3.setId(3);
        customerList = new ArrayList<>();
        customerList.add(customer1);
        customerList.add(customer2);

        Mockito.when(customerDAOMock.findAll()).thenReturn(customerList);
    }

    @Test
    public void registerNewCustomerTest_shouldRegister(){

        customerService.registerNewCustomer(customer3);

        Mockito.verify(customerDAOMock).save(customer3);

    }

    @Test
    public void registerNewCustomerTest_shouldReturnThatCustomerAlreadyExist(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->{

            customerService.registerNewCustomer(customer1);
        });

        assertThat(thrown.getMessage(), is(equalTo("Customer already exists")));
    }

    @Test
    public void deleteCustomerTest_shouldDelete(){

        boolean returnedValue = customerService.deleteCustomer(1);

        assertThat(returnedValue, is(equalTo(true)));
    }

    @Test
    public void deleteCustomerTest_shouldInformThatListHasNoCustomers(){

        customerList.remove(1);
        customerList.remove(0);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->{

            customerService.deleteCustomer(customer1.getId());
        });

        assertThat(thrown.getMessage(), is(equalTo("No customers in the list")));
    }

    @Test
    public void deleteCustomerTest_shouldInformThatCustomerDoesNotExist(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->{

            customerService.deleteCustomer(customer3.getId());
        });

        assertThat(thrown.getMessage(), is(equalTo("This customer does not exist to be deleted")));
    }

    @Test
    public void deleteCustomerTest_shouldInformThatCantBeDeletedBecauseLoans(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->{

            customerService.deleteCustomer(customer2.getId());
        });

        assertThat(thrown.getMessage(), is(equalTo("This costumer has books with him, can not be deleted")));
    }

    @Test
    public void updateCustomerTest_shouldUpdate(){

        boolean returnedValue = customerService.updateCustomer(customer1);

        assertThat(returnedValue, is(equalTo(true)));
    }

    @Test
    public void updateCustomerTest_shouldInformThatInputIsNull(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->{

            customerService.updateCustomer(null);
        });

        assertThat(thrown.getMessage(), is(equalTo("You can not save a customer whose instance does not exist")));
    }

    @Test
    public void updateCustomerTest_shouldInformThatThisCustomerDoesNotExist(){

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->{

            customerService.updateCustomer(customer3);
        });

        assertThat(thrown.getMessage(), is(equalTo("Try to create a new customer, this customer does not exist")));
    }

    @Test
    public void searchCustomerByIdTest_shouldReturnACustomer(){

        customerService.searchCustomerById(1);

        Mockito.verify(customerDAOMock).findById(1);
    }

    @Test
    public void listAllCustomersTest_shouldReturnAllCustomers(){

        Mockito.when(customerService.listAllCustomers()).thenReturn(customerList);

        var returnedValue = customerService.listAllCustomers();

        Mockito.verify(customerDAOMock).findAll();
        assertThat(returnedValue, is(equalTo(customerList)));
    }
}
