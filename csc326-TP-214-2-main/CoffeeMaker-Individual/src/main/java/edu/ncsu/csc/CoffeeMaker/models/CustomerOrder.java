package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * The class for storing orders. The order contains the customer who made the
 * order along with a
 *
 * @author Benjamin Yu & Luke Getzen
 *
 */
@Entity
public class CustomerOrder extends DomainObject {

    /** Database id id */
    @Id
    @GeneratedValue
    private Long         id;

    /** Order id */
    private final String orderId;

    /** Customer who ordered */
    public String      customer;

    /** The recipe being made */
    @OneToOne
    public Recipe        recipe;

    /** status of the customer order */
    public String        status;

    /**
     * Default constructor
     */
    public CustomerOrder () {
        orderId = "";
        status = "";
    }

    /**
     * Used for creating orders by customer c for recipe r with id i
     *
     * @param c
     *            - customer ordering
     * @param r
     *            - recipe being made
     * @param i
     *            - id for the order
     */
    public CustomerOrder ( final String c, final Recipe r, final String orderId ) {
        this.orderId = orderId;
        customer = c;
        recipe = r;
        status = "pending";

    }

    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Returns the order's id
     *
     * @return the order's orderId
     */
    public String getOrderId () {
        return orderId;
    }

    /**
     * return the status of the order
     *
     * @return string status
     */
    public String getStatus () {
        return status;
    }

    /**
     * set status to in progress for the order
     */
    public void setInProgress () {
        this.status = "in progress";
    }

    /*
     * set status to completed
     */
    public void setCompleted () {
        this.status = "completed";
    }

}
