package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Ingredient extends DomainObject {

    /** Ingredient id */
    @Id
    @GeneratedValue
    private Long    id;

    /** name of ingredient */
    @Autowired
    private String  name;

    /** Amount of ingredient */
    @Autowired
    private Integer amount;

    /** Default constructor */
    public Ingredient () {
        // Empty
    }

    /** Constructor for the ingredient */
    public Ingredient ( final String name, final Integer amount ) {

        setName( name );
        if ( checkAmount( amount ) ) {
            setAmount( amount );
        }
        else {
            throw new IllegalArgumentException( "Amount must be positive integer" );
        }
    }

    @Override
    public Serializable getId () {
        return id;
    }

    public Integer getAmount () {
        return amount;
    }

    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    public boolean checkAmount ( final Integer amount ) {
        if ( amount < 0 ) {
            return false;
        }
        else {
            return true;
        }
    }

    public String getName () {
        return name;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

}
