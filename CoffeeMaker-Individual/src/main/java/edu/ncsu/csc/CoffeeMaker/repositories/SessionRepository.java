package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Session;

/**
 * SessionRepository is used to provide CRUD operations for the Session model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Quaker Schneider
 *
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Finds a Session object with the provided token. Spring will generate code
     * to make this happen.
     *
     * @param token
     *            The token we are turning into a full session object
     * @return Found Session, null if none.
     */
    Session findByToken ( String token );

}
