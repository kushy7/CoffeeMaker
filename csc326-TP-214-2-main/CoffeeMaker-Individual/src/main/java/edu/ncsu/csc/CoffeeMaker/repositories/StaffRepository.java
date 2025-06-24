package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Staff;

/**
 * CRUD
 *
 * @author benyu
 *
 */
public interface StaffRepository extends JpaRepository<Staff, Long> {

    /**
     * Finds a Staff object with the provided name. Spring will generate code to
     * make this happen.
     *
     * @param username
     *            Name of the recipe
     * @return Found recipe, null if none.
     */
    Staff findByUsername ( String username );

}
