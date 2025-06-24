package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ncsu.csc.CoffeeMaker.models.Session;
import edu.ncsu.csc.CoffeeMaker.models.UserType;

/**
 * JUnit Test class for Session
 *
 * @author Yi Zhang
 *
 */
public class SessionTest {

    @Test
    public void testTokenLength () {
        final Session s = new Session( 1L, UserType.CUSTOMER );
        final String token = s.getToken();
        assertEquals( Session.TOKEN_LENGTH, token.length() );
    }

    @Test
    public void testValidAlphabet () {
        final Session s = new Session( 1L, UserType.CUSTOMER );
        final String token = s.getToken();
        for ( final char c : token.toCharArray() ) {
            assertTrue( Session.VALID_ALPHABET.indexOf( c ) != -1 );
        }
    }

    @Test
    public void testSessionUserId () {
        final Long userId = 1234L;
        final Session s = new Session( userId, UserType.CUSTOMER );
        assertEquals( userId, s.getUserId() );
    }

    @Test
    public void testSessionUserType () {
        final UserType userType = UserType.STAFF;
        final Session s = new Session( 1L, userType );
        assertEquals( userType, s.getUserType() );
    }

}
