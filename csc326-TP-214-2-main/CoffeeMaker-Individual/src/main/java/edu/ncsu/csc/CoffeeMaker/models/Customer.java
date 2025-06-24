package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Customer class to handle customer functionality
 *
 * @author lukeg
 *
 */
@Entity
public class Customer extends DomainObject {

    /** Customer id */
    @Id
    @GeneratedValue
    private Long   id;

    /** Customer name */
    private String username;

    /** Customer price */
    private String password;
    
    /** Active user session */
    private Long   sessionId;

    /**
    *
    */
    public Customer () {
        username = "";
        password = "";
        sessionId = -1L;
    }

    /**
     * constructor for customer
     *
     * @param username
     *            username
     * @param password
     *            password
     */
    public Customer ( final String username, final String password ) {
        this.setUsername( username );
        this.setPassword( password );
    }

    /**
     * Validates customer based on the inputted username and password. Returns
     * true if the credentials match and false if they do not
     *
     * @param username
     *            - username of the customer
     * @param password
     *            - password of the customer
     * @return true if credentials match and false otherwise
     */
    public boolean validate ( final String username, final String password ) {
        return ( this.username.equals( username ) && this.password.equals( password ) );
    }

    /**
     * method for ordering a beverage
     *
     * @return true if beverage successfully created
     */
    public boolean orderBeverage () {
        return true;
    }

    /**
     * method for if an order is ready
     *
     * @return true if order is ready
     */
    public boolean orderReady () {
        return true;
    }

    /**
     * pick up order method
     *
     * @return true if order has been picked up
     */
    public boolean pickupOrder () {
        return true;
    }

    /**
     * returns the username
     *
     * @return username
     */
    public String getName () {
        return username;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * returns the username
     *
     * @return username
     */
    public String getUsername () {
        return username;
    }

    /**
     * sets the username
     *
     * @param username
     *            to be set
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * returns the password
     *
     * @return password
     */
    public String getPassword () {
        return password;
    }

    /**
     * sets the password
     *
     * @param password
     *            to be set
     */
    public void setPassword ( final String password ) {
        if ( isValidPassword( password ) ) {
            this.password = password;
        }
    }

    /**
     * checks to see if a password is valid requirements are: must be 8
     * characters in length, must contain an upper case, must contain only
     * numbers and letters, must contain 2 numbers
     *
     * @param password
     *            to be checked
     * @return true if password is valid, false if not
     */
    public static boolean isValidPassword ( final String password ) {

        if ( password.length() < 8 ) {
            return false;
        }
        int charCount = 0;
        int numCount = 0;
        int capCount = 0;
        for ( int i = 0; i < password.length(); i++ ) {
            final char ch = password.charAt( i );

            if ( isUpperLetter( ch ) ) {
                capCount++;
            }
            if ( isLetter( ch ) ) {
                charCount++;
            }
            else if ( isNumeric( ch ) ) {
                numCount++;
            }

            else {
                return false;
            }
        }

        return ( charCount >= 2 && numCount >= 2 && capCount >= 1 );
    }

    /**
     * helper method to check if its a letter
     *
     * @param ch
     *            character to check
     * @return true if it is a letter
     */
    public static boolean isLetter ( final char ch ) {
        final char temp = Character.toUpperCase( ch );
        return ( temp >= 'A' && temp <= 'Z' );
    }

    /**
     * helper method to check if its an uppercase letter
     *
     * @param ch
     *            character to check
     * @return true if it is upper case
     */
    public static boolean isUpperLetter ( final char ch ) {
        return ( ch >= 'A' && ch <= 'Z' );
    }

    /**
     * helper method to check if it is a digit
     *
     * @param ch
     *            character to check
     * @return true if it is a digit
     */
    public static boolean isNumeric ( final char ch ) {

        return ( ch >= '0' && ch <= '9' );
    }
    
    /**
     * Gets the user's active session
     * @return the session associated with this user
     */
    public Long getSession() {
        return this.sessionId;
    }
    
    /**
     * Sets the user's active session
     * @param s the active session
     */
    public void setSession(final Long s) {
        this.sessionId = s;
    }

}
