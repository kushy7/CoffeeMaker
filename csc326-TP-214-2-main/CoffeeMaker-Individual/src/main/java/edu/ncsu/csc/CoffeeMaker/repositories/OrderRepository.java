package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;

/**
 * OrderRepository is used to provide CRUD operations for the Order model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    /**
     * Finds an Order object with the provided orderId. Spring will generate
     * code to make this happen.
     *
     * @param orderId
     *            - id of the order
     * @return Found order, null if none.
     */
    CustomerOrder findByOrderId ( String orderId );

}
