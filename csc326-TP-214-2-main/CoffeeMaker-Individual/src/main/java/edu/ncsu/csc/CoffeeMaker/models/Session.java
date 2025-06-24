package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.security.SecureRandom;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class for holding session information related to user auth
 *
 * @author Quaker Schneider
 */
@Entity
public class Session extends DomainObject {

    /** The length of our token in chars **/
    public static final int TOKEN_LENGTH = 64;

    /** The length of our token in chars **/
    public static final String VALID_ALPHABET = 
            "abcdefghijklmnopqrstuvqxyz" + 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + 
            "0123456789";
    
    /** This session's db id */
    @Id
    @GeneratedValue
    private Long id;
    
    /** The session token for authentication */
    private String token;

    /** This session's user's db id */
    private Long userId;
    
    /** This session's user's type */
    @Enumerated(EnumType.ORDINAL)
    private UserType userType;

    /**
    * Empty session constructor
    */
    public Session () {
        token = null;
        userId = -1L;
        userType = null;
    }
    
    /**
     * session constructor
     * 
     * @param userId the user id to associate with this session
     */
    public Session (Long userId, UserType type) {
        final SecureRandom r = new SecureRandom();
        String t = "";
        for(int i = 0; i < TOKEN_LENGTH; ++i) {
            t += VALID_ALPHABET.charAt( r.nextInt( VALID_ALPHABET.length() ) );
        }
        this.token = t;
        this.userId = userId;
        this.userType = type;
    }

    @Override
    public Serializable getId () {
        return this.id;
    }

    public Serializable getUserId() {
        return this.userId;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public String getToken () {
        return this.token;
    }

}
