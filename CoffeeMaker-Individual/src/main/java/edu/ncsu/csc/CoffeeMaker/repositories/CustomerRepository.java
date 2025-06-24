package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Customer;

/**
 * RecipeRepository is used to provide CRUD operations for the Recipe model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Lucas Getzen
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a Customer object with the provided name. Spring will generate code
     * to make this happen.
     *
     * @param username
     *            Name of the recipe
     * @return Found Customer, null if none.
     */
    Customer findByUsername ( String username );

}
