package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Staff class
 *
 * @author benyu & lmgetzen
 *
 */
@Entity
public class Staff extends DomainObject {

    /** Staff id */
    @Id
    @GeneratedValue
    private Long         id;
    /** Username for the customer */
    private final String username;
    /** Password for the customer */
    private String       password;

    /** Whether or not the user is a manager */
    boolean              manager;

    private final String userAdminPassword;
    
    /** Active user session */
    private Long   sessionId;


    /**
     *
     */
    public Staff () {
        username = "";
        password = "";
        userAdminPassword = "";
        manager = false;
    }

    /**
     * Creates a Staff object with the given parameters
     *
     * @param username
     *            - username of the staff
     * @param password
     *            - password of the staff
     * @param manager
     *            - whether or not the staff is a manager
     */
    public Staff ( final String username, final String password, final boolean manager, final String userAdminPass ) {
        this.username = username;
        setPassword( password );
        this.manager = manager;
        this.userAdminPassword = userAdminPass;

    }

    public String getUserAdminPassword () {
        return userAdminPassword;
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
     * Returns whether the staff is a manager or not
     *
     * @return whether the staff is a manager or not
     */
    public boolean isManager () {
        return manager;
    }

    /**
     * Returns the username of the staff
     *
     * @return the username of the staff
     */
    public String getUsername () {
        return username;
    }

    /**
     * Returns the password of the staff
     *
     * @return the password of the staff
     */
    public String getPassword () {
        return password;
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

    /**
     * Sets the password and authenticates it
     *
     * @param password
     *            - inputted password
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

}
