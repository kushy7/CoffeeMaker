package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Session;
import edu.ncsu.csc.CoffeeMaker.models.UserType;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.SessionRepository;

/**
 * The CustomerService is used to handle CRUD operations on the Customer model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Customer by name.
 *
 * @author Lucas Getzen
 * @author Quaker Schneider
 */
@Component
@Transactional
public class CustomerService extends Service<Customer, Long> {

    /**
     * CustomerRepository, to be autowired in by Spring and provide CRUD
     * operations on Recipe model.
     */
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * SessionRepository, to be autowired in by Spring and provide CRUD
     * operations on Session model.
     */
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    protected JpaRepository<Customer, Long> getRepository () {
        return customerRepository;
    }

    /**
     * Find a recipe with the provided name
     *
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public Customer findByUsername ( final String username ) {
        return customerRepository.findByUsername( username );
    }
    
    /**
     * Authenticates a user using a username and password and returns a session token
     * 
     * @param username the username
     * @param password the password
     * @return the session token of the user's log in, or null if login fails.
     */
    public String authenticateCustomer ( final String username, final String password ) {
        Customer c = customerRepository.findByUsername( username );
        if(c == null || !c.getPassword().equals(password)) return null;
        final Session s = new Session(c.getId(), UserType.CUSTOMER);
        sessionRepository.save( s );
        c.setSession( (Long) s.getId() );
        customerRepository.save( c );
        return sessionRepository.getOne( c.getSession() ).getToken();
    }

}
