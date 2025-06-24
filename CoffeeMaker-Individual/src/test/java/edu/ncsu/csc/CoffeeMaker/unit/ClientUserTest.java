package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import edu.ncsu.csc.CoffeeMaker.models.ClientUser;
import edu.ncsu.csc.CoffeeMaker.models.UserType;

/**
 * JUnit Test class for ClientUser
 *
 * @author Yi Zhang
 *
 */
public class ClientUserTest {

    @Test
    public void testConstructor () {
        final ClientUser user = new ClientUser();
        assertNull( user.username );
        assertNull( user.type );
    }

    @Test
    public void testParameterizedConstructor () {
        final String username = "johndoe";
        final UserType userType = UserType.CUSTOMER;
        final ClientUser user = new ClientUser( username, userType );
        assertEquals( username, user.username );
        assertEquals( userType, user.type );
    }

}
