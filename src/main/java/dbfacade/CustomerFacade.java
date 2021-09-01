/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbfacade;

import entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author abdi0747
 */
public class CustomerFacade {
    
    private static EntityManagerFactory emf;
    private static CustomerFacade instance;
    
    private CustomerFacade(){}
    
    public static CustomerFacade getCustomerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CustomerFacade();
        }
        return instance;
    }
    
    public Customer findByID(int id){
        EntityManager em = emf.createEntityManager();
        try{
            Customer customer = em.find(Customer.class, id);
            return customer;
        }
        finally{
            em.close();
        }
    }
    
    public List<Customer> findByLastName(String name){
        EntityManager em = emf.createEntityManager();
        try{
           TypedQuery<Customer> query = 
                   em.createQuery("Select last_name from Customer customer",Customer.class);
           return query.getResultList();
        }
        finally{
            em.close();
        }
    }
    
    public int getNumberOfCustomers(){
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("Select COUNT(customer) from Customer customer");
             return  query.getFirstResult();
        } finally {
            em.close();
        }
    }
    
    public List<Customer> allCustomers(){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Customer> query = 
                    em.createQuery("Select Customer from Customer customer", Customer.class);
            return query.getResultList();
        }
        finally{
            em.close();
        }
    }
    
    public Customer addName(String fName, String lName){
        Customer customer = new Customer(fName, lName);
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return customer;
        }
        finally{
            em.close();
        }
    }
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu3");
        CustomerFacade facade = CustomerFacade.getCustomerFacade(emf);
        Customer customer1 = facade.addName("Cristiano", "Ronaldo");
        Customer customer2 = facade.addName("Lionel", "Messi");
        Customer customer3 = facade.addName("Zlatan", "Ibrahimovic");
        System.out.println("Customer1: " + facade.findByID(customer1.getId()).getLastName());
        System.out.println("Customer2: " + facade.findByID(customer2.getId()).getLastName());
        System.out.println("Customer3: " + facade.findByID(customer3.getId()).getLastName());
        System.out.println("Number of customers: " + facade.allCustomers().size());
    }

}
