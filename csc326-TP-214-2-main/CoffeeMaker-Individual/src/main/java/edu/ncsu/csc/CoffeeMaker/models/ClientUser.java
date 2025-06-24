package edu.ncsu.csc.CoffeeMaker.models;

/**
 * Represents a user object
 * 
 * For returning in HTTP requests to give barebones info to the client
 * about an authenticated user.
 *
 * @author Quaker Schneider
 */
public class ClientUser {
    
    /** Customer name */
    public String username;
    
    /** The type this user is */
    public UserType type;

    /**
    * Constructs a new ClientUser object
    */
    public ClientUser() {
        this.username = null;
        this.type = null;
    }

    /**
    * Constructs a new ClientUser object
    * @param usernameIn the user's username
    * @param typeIn     the user's type (customer or staff)
    */
    public ClientUser (String usernameIn, UserType typeIn) {
        username = usernameIn;
        type = typeIn;
    }

}
