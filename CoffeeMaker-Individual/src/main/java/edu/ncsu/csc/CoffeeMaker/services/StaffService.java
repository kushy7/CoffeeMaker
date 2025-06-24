package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Session;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.UserType;
import edu.ncsu.csc.CoffeeMaker.repositories.SessionRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;

/**
 * CRUD
 *
 * @author benyu
 * @author Quaker Schneider
 */
@Component
@Transactional
public class StaffService extends Service<Staff, Long> {

    /**
     * StaffRepository, to be autowired in by Spring and provide CRUD operations
     * on Recipe model.
     */
    @Autowired
    private StaffRepository staffRepository;

    /**
     * SessionRepository, to be autowired in by Spring and provide CRUD
     * operations on Session model.
     */
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    protected JpaRepository<Staff, Long> getRepository () {
        return staffRepository;
    }

    /**
     * Find a recipe with the provided name
     *
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public Staff findByName ( final String name ) {
        return staffRepository.findByUsername( name );
    }
    
    /**
     * Authenticates a user using a username and password and returns a session token
     * 
     * @param username the username
     * @param password the password
     * @return the session token of the user's log in, or null if login fails.
     */
    public String authenticateStaff ( final String username, final String password ) {
        Staff c = staffRepository.findByUsername( username );
        if(c == null || !c.getPassword().equals(password)) return null;
        final Session s = new Session(c.getId(), UserType.STAFF);
        sessionRepository.save( s );
        c.setSession( (Long) s.getId() );
        staffRepository.save( c );
        return sessionRepository.getOne( c.getSession() ).getToken();
    }

}
