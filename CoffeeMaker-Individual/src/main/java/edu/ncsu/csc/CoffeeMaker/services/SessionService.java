package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.ClientUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Session;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.UserType;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.SessionRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;

/**
 * The SessionService is used to handle CRUD operations on the Session model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Session by token.
 *
 * @author Quaker Schneider
 */
@Component
@Transactional
public class SessionService extends Service<Session, Long> {

    /**
     * CustomerRepository, to be autowired in by Spring and provide CRUD
     * operations on Customer model.
     */
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * StaffRepository, to be autowired in by Spring and provide CRUD
     * operations on Staff model.
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
    protected JpaRepository<Session, Long> getRepository () {
        return sessionRepository;
    }

    /**
     * Find a recipe with the provided name
     *
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public ClientUser findUser ( final String token ) {
        ClientUser out = new ClientUser();
        final Session s = sessionRepository.findByToken( token );
        out.type = s.getUserType();
        if(s.getUserType() == UserType.CUSTOMER) {
            // Customer Session
            final Customer user = customerRepository.findById( (long) s.getUserId() ).orElse( null );
            if(user == null) {
                // This should never happen
                // TODO: Add logging step here?
                return null;
            }
            out.username = user.getName();
        } else {
            // Staff Session
            final Staff user = staffRepository.findById( (long) s.getUserId() ).orElse( null );
            if(user == null) {
                // This should never happen
                // TODO: Add logging step here?
                return null;
            }
            out.username = user.getUsername();
        }
        return out;
    }

    /**
     * Removes a session from the database
     * @param session the token of the session to remove
     */
    public void endSession ( String token ) {
        final Session s = sessionRepository.findByToken( token );
        if(s == null) {
            return;
        }
        sessionRepository.delete( s );
        if(s.getUserType() == UserType.CUSTOMER) {
            // Customer Session
            final Customer user = customerRepository.findById( (long) s.getUserId() ).orElse( null );
            if(user == null) {
                // This should never happen
                // TODO: Add logging step here?
                return;
            }
            user.setSession( -1L );
            customerRepository.save( user );
        } else {
            // Staff Session
            final Staff user = staffRepository.findById( (long) s.getUserId() ).orElse( null );
            if(user == null) {
                // This should never happen
                // TODO: Add logging step here?
                return;
            }
            user.setSession( -1L );
            staffRepository.save( user );
        }
    }

}
