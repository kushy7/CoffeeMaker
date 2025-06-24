package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.SessionRepository;

/**
 * CRUD
 *
 * @author benyu
 * @author Quaker Schneider
 */
@Component
@Transactional
public class OrderService extends Service<CustomerOrder, Long> {

    /**
     * orderRepository, to be autowired in by Spring and provide CRUD operations
     * on Recipe model.
     */
    @Autowired
    private OrderRepository   orderRepository;

    /**
     * SessionRepository, to be autowired in by Spring and provide CRUD
     * operations on Session model.
     */
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    protected JpaRepository<CustomerOrder, Long> getRepository () {
        return orderRepository;
    }

    /**
     * Find an Order with the provided orderId
     *
     * @param orderId
     *            id of the order to find
     * @return found ordre, null if none
     */
    public CustomerOrder findByOrderId ( final String orderId ) {
        return orderRepository.findByOrderId( orderId );
    }

}
